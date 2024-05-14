DROP TABLE IF EXISTS `dislike_ingredient`;
DROP TABLE IF EXISTS `fridge_ingredient`;
DROP TABLE IF EXISTS `recipe_favorite`;
DROP TABLE IF EXISTS `recipe_score`;

DROP TABLE IF EXISTS `image`;
DROP TABLE IF EXISTS `views`;
DROP TABLE IF EXISTS `likes`;
DROP TABLE IF EXISTS `comments`;
DROP TABLE IF EXISTS `board_tag`;
DROP TABLE IF EXISTS `tag`;

DROP TABLE IF EXISTS `board`;
DROP TABLE IF EXISTS `normal_member`;
DROP TABLE IF EXISTS `social_member`;
DROP TABLE IF EXISTS `cloth_member`;
DROP TABLE IF EXISTS `member`;


-- member
CREATE TABLE member
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    birthday        DATETIME(6)                                                                 NOT NULL,
    email           VARCHAR(255)                                                                NOT NULL,
    gender          VARCHAR(255)                                                                NOT NULL,
    name            VARCHAR(255)                                                                NOT NULL,
    nickname        VARCHAR(255)                                                                         DEFAULT 'DEFAULTUSERNICKNAME' NOT NULL,
    profile_picture VARCHAR(255)                                                                NULL,
    role            ENUM ('ROLE_ADMIN', 'ROLE_REGISTERED_MEMBER', 'ROLE_NON_REGISTERED_MEMBER') NOT NULL,
    created_at      DATETIME                                                                    NOT NULL DEFAULT NOW(),
    updated_at      DATETIME                                                                    NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    CONSTRAINT UNIQUE (email)
);


-- social member
CREATE TABLE social_member
(
    id          BIGINT                   NOT NULL PRIMARY KEY,
    social_type ENUM ('GOOGLE', 'KAKAO') NOT NULL,
    created_at  DATETIME                 NOT NULL DEFAULT NOW(),
    updated_at  DATETIME                 NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    CONSTRAINT FK_SOCIAL_MEMBER_MEMBER_ID FOREIGN KEY (id) REFERENCES member (id) ON DELETE CASCADE
);


-- normal_member
CREATE TABLE normal_member
(
    ID         BIGINT       NOT NULL PRIMARY KEY,
    PASSWORD   VARCHAR(255) NOT NULL,
    created_at DATETIME     NOT NULL DEFAULT NOW(),
    updated_at DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    CONSTRAINT FK_NORMAL_MEMBER_MEMBER_ID FOREIGN KEY (ID) REFERENCES member (ID) ON DELETE CASCADE
);

-- ###################
-- about board
-- ###################

-- board
CREATE TABLE board
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT      NOT NULL,
    title      VARCHAR(50) NOT NULL,
    content    TEXT        NOT NULL,
    created_at DATETIME    NOT NULL DEFAULT NOW(),
    updated_at DATETIME    NOT NULL DEFAULT NOW() ON UPDATE NOW()
);


-- comments
CREATE TABLE comments
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    board_id   BIGINT   NULL,
    member_id  BIGINT   NULL,
    content    TEXT     NULL,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    CONSTRAINT FK_COMMENTS_BOARD_ID FOREIGN KEY (board_id) REFERENCES board (id) ON DELETE CASCADE,
    CONSTRAINT FK_COMMENTS_MEMBER_ID FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
);


-- views
CREATE TABLE views
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    board_id   BIGINT   NOT NULL,
    member_id  BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    CONSTRAINT FK_VIEWS_BOARD_ID FOREIGN KEY (board_id) REFERENCES board (id) ON DELETE CASCADE,
    CONSTRAINT FK_VIEWS_MEMBER_ID FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
);

-- likes
CREATE TABLE likes
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    board_id   BIGINT   NOT NULL,
    member_id  BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    CONSTRAINT FK_LIKES_BOARD_ID FOREIGN KEY (board_id) REFERENCES board (id),
    CONSTRAINT FK_LIKES_MEMBER_ID FOREIGN KEY (member_id) REFERENCES member (id)
);


-- tag
CREATE TABLE tag
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(10) NOT NULL,
    created_at DATETIME    NOT NULL DEFAULT NOW(),
    updated_at DATETIME    NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    CONSTRAINT FK_TAG_BOARD_ID UNIQUE (name)
);


-- board_tag
CREATE TABLE board_tag
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    tag_id     BIGINT   NULL,
    board_id   BIGINT   NULL,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    CONSTRAINT FK_BOARD_TAG_ID FOREIGN KEY (tag_id) REFERENCES tag (id),
    CONSTRAINT FK_TAG_BOARD_ID FOREIGN KEY (board_id) REFERENCES board (id)
);

-- image
CREATE TABLE image
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    reference_id BIGINT                           NOT NULL,
    address      VARCHAR(255)                     NULL,
    image_type   ENUM ('BOARD','PROFILE_PICTURE') NULL,
    created_at   DATETIME                         NOT NULL DEFAULT NOW(),
    updated_at   DATETIME                         NOT NULL DEFAULT NOW() ON UPDATE NOW()
);

-- ###################
-- about recipe
-- ###################

CREATE TABLE dislike_ingredient
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT      NOT NULL,
    name      VARCHAR(50) NOT NULL,
    CONSTRAINT FK_DISLIKE_INGREDIENT_MEMBER
        FOREIGN KEY (member_id) REFERENCES member (id)
            ON DELETE CASCADE
);

CREATE TABLE fridge_ingredient
(
    id        BIGINT AUTO_INCREMENT
        PRIMARY KEY,
    member_id BIGINT      NOT NULL,
    end_at    DATE        NOT NULL,
    name      VARCHAR(50) NOT NULL,
    CONSTRAINT FK_FRIDGE_INGREDIENT_MEMBER
        FOREIGN KEY (member_id) REFERENCES member (id)
            ON DELETE CASCADE
);


CREATE TABLE recipe_favorite
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    CONSTRAINT FK_RECIPE_FAVORITE_MEMBER
        FOREIGN KEY (member_id) REFERENCES member (id)
            ON DELETE CASCADE
);

CREATE TABLE recipe_score
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id bigint NOT NULL,
    score     int    NOT NULL,
    recipe_id bigint NOT NULL,
    CONSTRAINT FK_RECIPE_SCORE_MEMBER
        FOREIGN KEY (member_id) REFERENCES member (id)
            ON DELETE CASCADE
);


-- cloth_member
CREATE TABLE cloth_member
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    cloth_id   BIGINT   NOT NULL,
    member_id  BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    CONSTRAINT FK_CLOTH_ID
        FOREIGN KEY (cloth_id) REFERENCES cloth (id)
            ON DELETE CASCADE,
    CONSTRAINT FK_MEMBER_ID
        FOREIGN KEY (member_id) REFERENCES member (id)
            ON DELETE CASCADE
);
