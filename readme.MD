# 미니 프로젝트2 포스트
### 작성 팀원 : 구강현, 김강빈

- 서비스 포트 : 8081

## 게시글 작성 시스템 구축
  - 게시글 생성
    - /post/create
      - 게시글 type : public, group, groupOnly
        ``` JSON
        {
          "type": "public | group | groupOnly",
          "content": "게시글 내용",
          "tag": "태그명",
          "image": "이미지 경로"
        }
        ```
  - 게시글 수정
    - /post/modify/{pid}
      ``` JSON
      {
        "type": "public | group | groupOnly",
        "content": "게시글 내용"
        "tag": "태그명",
        "image": "이미지 경로"
      }
      ```  
  - 게시글 삭제
    - /post/delete/{pid}
  - 좋아요 기능
    - /post/toggleLike/{pid}

## 댓글 시스템
  - 댓글 생성 
    - /cmt/create/{pid}
      ``` JSON
      {
        "content": "댓글 내용"
      }
      ```
  - 댓글 수정 
    - /cmt/modify/{cid}
      ``` JSON
      {
        "content": "댓글 내용"
      }
      ```
  - 댓글 삭제 
    - /cmt/delete/{cid}
  - 전체 댓글 조회 
    - /cmt/getAllComments/{pid}
  - 특정 댓글 조회 
    - /cmt/getPidComments/{pid}


## 검색 시스템 구축
  - 검색(/search)
    - type : tag -> 태그 기반 검색
    - type : msg -> 내용 기반 검색
    - type : email -> 이메일 기반 검색
    - msg : 검색 내용
      ``` JSON
      {
        "type": "tag | msg | email",
        "msg": "검색 내용"
      }
      ```
