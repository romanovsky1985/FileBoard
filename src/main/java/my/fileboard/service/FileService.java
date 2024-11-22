package my.fileboard.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import my.fileboard.entity.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class FileService {
    @Autowired
    private EntityManager entityManager;

    public List<FileEntity> getSharedFiles() {
        return entityManager
                .createQuery("select f from FileEntity f where f.shared = true", FileEntity.class)
                .getResultList();
    }

    public FileEntity getSharedEntity(Long id) {
        FileEntity entity = entityManager.find(FileEntity.class, id);
        if (entity == null) {
            throw new IllegalArgumentException("file not found");
        } else if (!entity.getShared()) {
            throw new IllegalArgumentException("access denied");
        } else {
            return entity;
        }
    }

    public byte[] getSharedData(Long id) {
        FileEntity entity = getSharedEntity(id);
        return entity.getData();
    }

    public List<FileEntity> getPersonalFiles(String owner) {
        return entityManager
                .createQuery("select f from FileEntity f where f.owner = :owner", FileEntity.class)
                .setParameter("owner", owner)
                .getResultList();
    }

    public FileEntity getPersonalEntity(String owner, Long id) {
        FileEntity entity = entityManager.find(FileEntity.class, id);
        if (entity == null) {
            throw new IllegalArgumentException("file not found");
        } else if (!Objects.equals(owner, entity.getOwner())) {
            throw new IllegalArgumentException("access denied");
        } else {
            return entity;
        }
    }

    public byte[] getPersonalData(String owner, Long id) {
        FileEntity entity = getPersonalEntity(owner, id);
        return entity.getData();
    }

    @Transactional
    public void switchPersonalAccess(String owner, Long id) {
        FileEntity entity = getPersonalEntity(owner, id);
        entity.setShared(!entity.getShared());
    }

    @Transactional
    public void deletePersonalFile(String owner, Long id) {
        FileEntity entity = getPersonalEntity(owner, id);
        entityManager.remove(entity);
    }

    @Transactional
    public void appendPersonalFile(String owner, String name, byte[] data) {
        FileEntity entity = new FileEntity();
        entity.setName(name);
        entity.setOwner(owner);
        entity.setData(data);
        entity.setShared(true);
        entity.setInfo(String.format("%d Kb", data.length / 1000 + 1));
        entityManager.persist(entity);
    }


}
