# 미니 프로젝트2 포스트
- 서비스 포트 : 8081
- 사용 데이터베이스 : mysql
- 데이터 베이스 정보
  - 데이터베이스 이름 : mini-post
  - 게시글 테이블 이름 : post
  - 좋아요 테이블 이름 : likes
  - 댓글 테이블 이름 : comment

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
        "image": "https://picsum.photos/200"
      }
      ```
  - 게시글 수정
    - /post/modify/{pid}
      ``` JSON
      {
        "type": "public",
        "content": "첫 게시글"
      }
      ```  
  - 게시글 삭제
    - /post/delete/{pid}
  - 좋아요 기능
    - /post/toggleLike/{pid}/{uid}

## 김강빈
- 댓글 시스템
  - 생성 (/cmt/create/{pid})
  - 수정 (/cmt/modify/{cid})
  - 삭제 (/cmt/delete/{cid})
  - 전체 조회 (/cmt/getAllComments/{pid})
- 
- 검색 시스템 구축
  - 검색(/search)
