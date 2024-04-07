package fr.uga.l3miage.spring.tp3.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class TestCenterRequest {
    private Long testCenterId;
    private Set<Long> studentIds;
}
