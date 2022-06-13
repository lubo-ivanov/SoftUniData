package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ApartmentSeedDto;
import softuni.exam.models.dto.ApartmentSeedRootDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class ApartmentServiceImpl implements ApartmentService {
    private static final String APARTMENT_FILE_PATH = "src/main/resources/files/xml/apartments.xml";
    private final ApartmentRepository apartmentRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final TownRepository townRepository;

   @Autowired
    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, TownRepository townRepository) {
        this.apartmentRepository = apartmentRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.townRepository = townRepository;
    }


    @Override
    public boolean areImported() {
        return apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(Path.of(APARTMENT_FILE_PATH));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        StringBuilder stringBuilder = new StringBuilder();
        List<ApartmentSeedDto> apartmentSeedDtos = xmlParser.fromFile(APARTMENT_FILE_PATH, ApartmentSeedRootDto.class).getApartments();

        for (ApartmentSeedDto apartmentSeedDto : apartmentSeedDtos) {
            boolean isValid = validationUtil.isValid(apartmentSeedDto);
            if (!isValid) {
                stringBuilder.append("Invalid Аpartment\n");
            } else {
                if (apartmentRepository
                        .findByAreaAndTownName(apartmentSeedDto.getArea(),
                                apartmentSeedDto.getTown())
                        .isPresent()) {
                    stringBuilder.append("Invalid Apartment\n");
                    continue;
                }
                Apartment apartment = modelMapper.map(apartmentSeedDto, Apartment.class);
                Optional<Town> town = townRepository.findByName(apartmentSeedDto.getTown());
                town.ifPresent(apartment::setTown);
                apartmentRepository.save(apartment);
                stringBuilder.append(String.format("Successfully imported apartment %s - %.2f\n",
                        apartmentSeedDto.getApartmentType(), apartmentSeedDto.getArea()));
            }
        }
                
        return stringBuilder.toString().trim();
    }
}
