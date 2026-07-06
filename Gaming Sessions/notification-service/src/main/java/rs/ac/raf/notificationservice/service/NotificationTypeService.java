package rs.ac.raf.notificationservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.raf.notificationservice.dto.NotificationTypeRequest;
import rs.ac.raf.notificationservice.dto.NotificationTypeResponse;
import rs.ac.raf.notificationservice.entity.NotificationType;
import rs.ac.raf.notificationservice.exception.ConflictException;
import rs.ac.raf.notificationservice.exception.NotFoundException;
import rs.ac.raf.notificationservice.repository.NotificationTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationTypeService {

    private final NotificationTypeRepository repository;

    public NotificationTypeService(NotificationTypeRepository repository) {
        this.repository = repository;
    }

    public List<NotificationTypeResponse> listAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public NotificationTypeResponse create(NotificationTypeRequest request) {
        if (repository.existsByCode(request.getCode())) {
            throw new ConflictException("Tip notifikacije sa ovim kodom vec postoji");
        }
        NotificationType type = new NotificationType();
        applyRequest(type, request);
        return toResponse(repository.save(type));
    }

    @Transactional
    public NotificationTypeResponse update(Long id, NotificationTypeRequest request) {
        NotificationType type = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tip notifikacije ne postoji: " + id));
        applyRequest(type, request);
        return toResponse(repository.save(type));
    }

    private void applyRequest(NotificationType type, NotificationTypeRequest request) {
        type.setCode(request.getCode());
        type.setDescription(request.getDescription());
        type.setSubjectTemplate(request.getSubjectTemplate());
        type.setBodyTemplate(request.getBodyTemplate());
    }

    private NotificationTypeResponse toResponse(NotificationType type) {
        return new NotificationTypeResponse(type.getId(), type.getCode(), type.getDescription(),
                type.getSubjectTemplate(), type.getBodyTemplate());
    }
}
