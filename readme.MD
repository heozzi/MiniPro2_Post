# 미니 프로젝트2 포스트
- 서비스 포트 : 8081
- 데이터베이스 : mysql

## 구강현
- 게시글 작성 시스템 구축
  - 게시글 생성
    - /post/create
      ``` JSON
      {
        "uid": 3,
        "type": "private",
        "content": "Bye",
        "tag": "free",
        "image": "https://picsum.photos/200​"
      }
      ```
  - 게시글 수정
    - /post/modify/{pid}
  - 게시글 삭제
    - /post/delete/{pid}
  - 좋아요 기능
    - /post/likeUp/{pid}
    - /post/likeDown/{pid}
- 데이터 베이스 정보
  - 데이터베이스 이름 : mini_post
  - 테이블 이름 : post

## 김강빈
- 댓글, 검색 시스템 구축
