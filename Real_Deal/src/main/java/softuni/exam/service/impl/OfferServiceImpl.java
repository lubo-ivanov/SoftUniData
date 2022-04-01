package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferSeedRootDto;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Offer;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class OfferServiceImpl implements OfferService {

    private static final String OFFER_FILE_PATH = "src/main/resources/files/xml/offers.xml";
    private final OfferRepository offerRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final CarRepository carRepository;
    private final SellerRepository sellerRepository;

    public OfferServiceImpl(OfferRepository offerRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil, CarRepository carRepository, SellerRepository sellerRepository) {
        this.offerRepository = offerRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.carRepository = carRepository;
        this.sellerRepository = sellerRepository;
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
        OfferSeedRootDto offerSeedRootDto = xmlParser.fromFile(OFFER_FILE_PATH, OfferSeedRootDto.class);
        offerSeedRootDto.getOffers().stream()
                .filter(offerRootDto -> {
                    boolean isValid = validationUtil.isValid(offerRootDto);
                    stringBuilder.append(isValid ? String.format("Successfully import offer %s - %s\n",
                            offerRootDto.getAddedOn(), offerRootDto.getHasGoldStatus())
                            : "Invalid offer\n");

                    return isValid;
                })
                .map(offerRootDto -> {
                    Offer offer = modelMapper.map(offerRootDto, Offer.class);
                    offer.setCar(carRepository.findById(offerRootDto.getCar().getId()).get());
                    offer.setSeller(sellerRepository.findById(offerRootDto.getSeller().getId()).get());
                    return offer;
                })
                .forEach(offerRepository::save);
        return stringBuilder.toString().trim();
    }
}
