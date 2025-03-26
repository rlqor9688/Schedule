USE schedule;

-- 테이블 생성(scheduleList)
CREATE TABLE scheduleList(
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT "스케줄 식별자",
    task VARCHAR(100) NOT NULL COMMENT "할 일",
    writer VARCHAR(50) COMMENT "작성자",
    password VARCHAR(50) COMMENT "비밀번호",
    created_date DATETIME COMMENT "작성일",
    modified_date DATETIME COMMENT "수정일"
);

-- 테이블 생성(user)
CREATE TABLE userinfo(
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT "유저 식별자",
    writer VARCHAR(50) COMMENT "작성자명",
    email VARCHAR(100) COMMENT "이메일 주소",
    created_date DATETIME COMMENT "작성일",
    modified_date DATETIME COMMENT "수정일"
);
