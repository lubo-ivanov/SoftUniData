package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AgentSeedDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.AgentService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AgentServiceImpl implements AgentService {
    private static final String AGENT_FILE_PATH = "src/main/resources/files/json/agents.json";
    private final AgentRepository agentRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final TownRepository townRepository;

    public AgentServiceImpl(AgentRepository agentRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil, TownRepository townRepository) {
        this.agentRepository = agentRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.townRepository = townRepository;
    }


    @Override
    public boolean areImported() {
        return agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files.readString(Path.of(AGENT_FILE_PATH));
    }

    @Override
    public String importAgents() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        AgentSeedDto[] agentSeedDtos = gson.fromJson(readAgentsFromFile(), AgentSeedDto[].class);
        for (AgentSeedDto agentSeedDto : agentSeedDtos) {
            boolean isValid = validationUtil.isValid(agentSeedDto);
            if (!isValid) {
                stringBuilder.append("Invalid Agent\n");
            } else {
                if (agentRepository
                        .findByFirstNameOrEmail(agentSeedDto.getFirstName(),
                                agentSeedDto.getEmail())
                        .isPresent()) {
                    stringBuilder.append("Invalid Agent\n");
                    continue;
                }
                Agent agent = modelMapper.map(agentSeedDto, Agent.class);
                Optional<Town> town = townRepository.findByName(agentSeedDto.getTown());
                town.ifPresent(agent::setTown);
                agentRepository.save(agent);
                stringBuilder.append(String.format("Successfully imported agent - %s %s\n",
                        agentSeedDto.getFirstName(), agentSeedDto.getLastName()));
            }
        }
        return stringBuilder.toString().trim();
    }
}

