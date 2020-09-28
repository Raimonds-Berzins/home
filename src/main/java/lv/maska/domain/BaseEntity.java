package lv.maska.domain;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.eclipse.jetty.util.security.Credential.MD5;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * DBBase
 */
@Slf4j
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @JsonIgnore
    String eTag;

    @EqualsAndHashCode.Include
    Boolean isActive;

    LocalDateTime created;

    LocalDateTime updated;
    
    private void setETag(){
        this.eTag = calkETag();
    }

    public String calkETag(){
        return MD5.digest(String.valueOf(this.hashCode())).replaceFirst("MD5:", "");
    }

    public void updateWithMap(Map<String, Object> propertyMap) throws Exception {
        for (Field field : this.getClass().getDeclaredFields()) {
            String fieldName = field.getName();
            if (!propertyMap.containsKey(fieldName))
                continue;
            
            Class<?> fieldType = field.getType();
            String fieldSetterMethod = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method method = this.getClass().getMethod(fieldSetterMethod, fieldType);
            Object obj = propertyMap.get(fieldName);

            if (fieldType.equals(obj.getClass())) {
                method.invoke(this, fieldType.cast(obj));

            } else if (fieldType.isEnum() && (obj instanceof String)) {
                String enumMethodName = "valueOf";
                Method enumMethod = fieldType.getMethod(enumMethodName, String.class);
                method.invoke(this, enumMethod.invoke(null, obj));
                
            } else
                throw new Exception("Wrong type for field:" + fieldName + " expected type:" + fieldType + " actual type:" + obj.getClass());
        }
    }

    @PrePersist
    void prePersist(){
        this.setCreated(LocalDateTime.now());
        this.setUpdated(LocalDateTime.now());
        this.setIsActive(true);
        this.setETag();
    }

    @PreUpdate
    void preUpdate(){
        this.setUpdated(LocalDateTime.now());
    }

    @PostLoad
    void postLoad(){
        if(this.getETag() != null && !this.getETag().equals(this.calkETag())){
            log.info(this.getClass() + ":" + this.getId() + " has been modified by DB - " + this.getETag() + ":" + this.calkETag());
            this.setETag();
        }
    }
}