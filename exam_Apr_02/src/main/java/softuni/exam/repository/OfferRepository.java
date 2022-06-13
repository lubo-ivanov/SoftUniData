package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Offer;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

   @Query(value = """
            SELECT o FROM Offer o 
            JOIN Apartment a ON o.apartment.id = a.id
            WHERE a.apartmentType = 'three_rooms'
            ORDER BY a.area DESC, o.price
           """)
    List<Offer> findByApartment_ApartmentTypeOrderByApartmentAreaDescThenByPriceAsc();

}
