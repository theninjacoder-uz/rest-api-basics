package com.epam.esm.repository.giftCertificate;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.repository.BaseRepo;

import java.util.List;

public interface GiftCertificateRepo extends BaseRepo<GiftCertificate> {

    List<GiftCertificate> findByParametersAndSort(
            String searchQuery,
            String tagName,
            String sortByDate,
            String sortByName
    );
}
