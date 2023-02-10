package com.example.assigneeauto.persistance.domain.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Getter
@Setter
public class BaseCacheEntity implements Serializable {

    @Id
    private String key;
}
