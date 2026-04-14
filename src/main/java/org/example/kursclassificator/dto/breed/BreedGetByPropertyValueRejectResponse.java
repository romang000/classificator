package org.example.kursclassificator.dto.breed;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class BreedGetByPropertyValueRejectResponse {

    private BreedResponse breed;

    private BreedGetByPropertyValueRejectionReasonResponse rejectReason;

}
