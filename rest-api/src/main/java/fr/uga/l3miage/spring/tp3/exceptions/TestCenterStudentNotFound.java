package fr.uga.l3miage.spring.tp3.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestCenterStudentNotFound {

        private String uri;
        private String errorMessage;
        private Long candidateId;
        private Long testCenterId;

}
