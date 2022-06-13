package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ApartmentSeedDto;
import softuni.exam.models.dto.ApartmentSeedRootDto;
import softuni.exam.models.dto.OfferSeedDto;
import softuni.exam.models.dto.OfferSeedRootDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Offer;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class OfferServiceImpl implements OfferService {

    private static final String OFFER_FILE_PATH = "src/main/resources/files/xml/offers.xml";
    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final ApartmentRepository apartmentRepository;
    private final AgentRepository agentRepository;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, ApartmentRepository apartmentRepository, AgentRepository agentRepository) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.apartmentRepository = apartmentRepository;
        this.agentRepository = agentRepository;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(OFFER_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder stringBuilder = new StringBuilder();
        List<OfferSeedDto> offerSeedDtos = xmlParser.fromFile(OFFER_FILE_PATH, OfferSeedRootDto.class).getOffers();

        for (OfferSeedDto offerSeedDto : offerSeedDtos) {
            boolean isValid = validationUtil.isValid(offerSeedDto);
            if (!isValid) {
                stringBuilder.append("Invalid offer\n");
            } else {
                Optional<Agent> agent = agentRepository.findByFirstName(offerSeedDto.getAgent().getName());
                if (agent.isEmpty()) {
                    stringBuilder.append("Invalid Offer\n");
                    continue;
                }
                Offer offer = modelMapper.map(offerSeedDto, Offer.class);
                Optional<Apartment> apartment = apartmentRepository.findById(offerSeedDto.getApartment().getId());
                offer.setAgent(agent.get());
                apartment.ifPresent(offer::setApartment);
                offerRepository.save(offer);
                stringBuilder.append(String.format("Successfully imported offer %.2f\n",
                        offerSeedDto.getPrice()));
            }
        }

        return stringBuilder.toString().trim();
    }

    @Override
    public String exportOffers() {

        StringBuilder builder = new StringBuilder();
        for (Offer offer : this.offerRepository
                .findByApartment_ApartmentTypeOrderByApartmentAreaDescThenByPriceAsc()) {
            builder.append(String.format("Agent %s %s with offer №%d:\n" +
                                         "   \t\t-Apartment area: %.2f\n" +
                                         "   \t\t--Town: %s\n" +
                                         "   \t\t---Price: %.2f$\n",
                    offer.getAgent().getFirstName(), offer.getAgent().getLastName(),
                    offer.getId(), offer.getApartment().getArea(), offer.getApartment().getTown().getName(),
                    offer.getPrice()));


        }
        return builder.toString();
    }
}