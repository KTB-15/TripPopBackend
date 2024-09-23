
# TripPop 🌏


![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Jpa](https://img.shields.io/badge/jpa-%236DB33F.svg?&style=for-the-badge&logo=jpa&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)


> 맞춤형 여행지 추천 서비스 TripPop

<img width="1692" alt="스크린샷 2024-09-23 오전 10 21 36" src="https://github.com/user-attachments/assets/312afcc3-4bfd-4c26-862e-f738d3518c9b">


## 📖 Description

여행을 가고싶지만 여행지를 고르지 못한 미래의 여행자에게 맞춤형 여행지를 추천합니다.

나이, 성별, 본인의 취향을 입력하고 맞는 여행지를 추천받으세요!

## :baby_chick: Demo
<p float="left">
    <img width="200" alt="스크린샷 2024-09-23 오전 10 25 42" src="https://github.com/user-attachments/assets/72686edd-7a89-434b-b69b-5d6e5be9325e">
    <img width="200" alt="스크린샷 2024-09-23 오전 10 26 22" src="https://github.com/user-attachments/assets/4a255e9a-8cf8-4b06-8d49-ab30637d55bf">
    <img width="200" alt="스크린샷 2024-09-23 오전 10 34 26" src="https://github.com/user-attachments/assets/1601ec17-6c65-4118-8458-601ad0388571">
    <img width="200" alt="스크린샷 2024-09-23 오전 10 36 36" src="https://github.com/user-attachments/assets/b6a20884-7506-43c8-a3ca-1917b3de6c67">
    <img width="200" alt="스크린샷 2024-09-23 오전 10 36 46" src="https://github.com/user-attachments/assets/1fc43755-be0e-4568-996c-2e7dd7b4931a">
    <img width="200" alt="스크린샷 2024-09-23 오전 10 56 43" src="https://github.com/user-attachments/assets/c4d3b1d6-5687-48d7-b4ac-3f2e41c0dffc">
</p>

## ⭐ Main Feature
### 회원가입 및 로그인
- base 로그인, OAuth2 로그인
- JWT 이용

### 여행지 추천
- JWT 이용

### 기타 기능
- 상품 리스트 조회 및 세부 사항 조회
- 마이페이지

## 🔧 Stack
- **Language**: Java
- **Library & Framework** : Spring Framework, JPA, Spring Security, OAuth2, JWT
- **Database** : PostgreSQL, Redis

## :open_file_folder: Project Structure

```markdown
src
├── common
│   └── exception
├── config
├── controller
├── dto
│   ├── auth
│   ├── favourite
│   ├── member
│   ├── oauth
│   ├── place
│   └── recommendation
├── entity
│   └── enums
├── jwt
├── repository
├── service
│   └── place
├── util
└── vo
```

## 🔨 DB 설계
### 기존 DB
<img width="1122" alt="스크린샷 2024-09-23 오후 1 17 25" src="https://github.com/user-attachments/assets/bc96b484-75cd-4d2a-8ee9-ce7d77c610a2">

### 변경한 DB
<img width="485" alt="스크린샷 2024-09-23 오후 1 18 21" src="https://github.com/user-attachments/assets/8e34f713-ad7b-49c3-ab43-d6de12abed9f">

## 🔨 이미지 응답
<img width="811" alt="스크린샷 2024-09-23 오후 1 22 19" src="https://github.com/user-attachments/assets/b5fea3cb-8233-4169-8fad-fb26cf47f4b8">


## 🔨 OAuth2 시퀀스 다이어 그램
<img width="530" alt="스크린샷 2024-09-23 오후 1 19 48" src="https://github.com/user-attachments/assets/856b053b-88db-471d-a385-98e9a682db90">


## 🔨 etc
- PR을 통한 코드 개선

## 👨‍👩‍👧‍👦 Developer
*  **한신** ([Urchinode](https://github.com/Urchinode))
*  **김영진** ([KmYgJn](https://github.com/KmYgJn))
