package com.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
/**
 * 사용자 정보는 id, email, name, createdDate, modifiedDate를 데이터 속성(컬럼)으로 가짐
 */
public class Userinfo {
    private Long id;
    private String email;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
