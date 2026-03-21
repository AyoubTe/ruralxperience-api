package com.ruralxperience.repository;

import com.ruralxperience.entity.Experience;
import com.ruralxperience.enums.ExperienceStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExperienceSpecification {

    public static Specification<Experience> buildSearchSpec(
            String keyword,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer minDuration,
            Integer maxDuration,
            Integer minGuests,
            ExperienceStatus status) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Default to PUBLISHED unless admin passes explicit status
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            } else {
                predicates.add(cb.equal(root.get("status"), ExperienceStatus.PUBLISHED));
            }

            if (keyword != null && !keyword.isBlank()) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("shortDescription")), pattern),
                    cb.like(cb.lower(root.get("location")), pattern)
                ));
            }

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("pricePerPerson"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("pricePerPerson"), maxPrice));
            }

            if (minDuration != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("durationDays"), minDuration));
            }

            if (maxDuration != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("durationDays"), maxDuration));
            }

            if (minGuests != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("maxGuests"), minGuests));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
