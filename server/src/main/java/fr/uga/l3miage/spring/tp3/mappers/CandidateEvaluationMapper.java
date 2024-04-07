package fr.uga.l3miage.spring.tp3.mappers;

import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.responses.CandidateEvaluationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ExamMapper.class})
public interface CandidateEvaluationMapper {
    CandidateEvaluationResponse toResponse(CandidateEvaluationGridEntity entity);
}