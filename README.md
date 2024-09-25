
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

여행을 가고싶지만 여행지를 고르지 못한 미래의 여행자에게 **AI가 맞춤형 여행지를 추천**합니다.

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
- base 로그인, **OAuth2** 로그인
- JWT 이용

### 이미지 응답
- **Google API**를 활용하여 AI가 추천해준 장소에 대한 이미지 응답
- 즐겨찾기

## 🔧 Stack
- **Language**: Java
- **Library & Framework** : Spring Framework, JPA, Spring Security, OAuth2, JWT
- **Database** : PostgreSQL, Redis, S3
- **API** : Google Maps API

## Architecture
<img width="700" alt="trippop" src="https://github.com/user-attachments/assets/935dd588-35bc-47f6-9f0b-aa7364dcb539">



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
공공데이터를 활용했고, 공공데이터의 csv 파일이 다음과 같았습니다.
<img width="1122" alt="스크린샷 2024-09-23 오후 1 17 25" src="https://github.com/user-attachments/assets/bc96b484-75cd-4d2a-8ee9-ce7d77c610a2">

### 변경한 DB
csv 파일에서 서비스에 필요한 정보만을 뽑아 ER 다이어그램을 설계하였습니다.

csv -> DB 적재 과정은 아래 레포지토리에서 진행했습니다.

https://github.com/KTB-15/TripPopData

<img height="700" alt="스크린샷 2024-09-23 오후 1 17 25" src="https://github.com/user-attachments/assets/f389a45a-66bf-4427-82b8-bed99759521a">


## 🔨 이미지 응답

공공데이터에서 여행지 이미지를 제공하지만,
**용량이 1TB** 이상인 것과, 여행지에 적합하지 않은 **개인의 일상이 담긴 이미지**가 많은 문제가 있었습니다.

이에 대해, 장소의 **좌표를 기반으로 이미지를 제공하는 Google Maps API**를 활용하여,
**S3**와 함께 기능을 고도화 했습니다.
흐름은 다음과 같습니다.
<img width="2386" alt="trippop-image" src="https://github.com/user-attachments/assets/4f1bbc81-eddc-41dc-b365-6799e9a2c54c">



## 🔨 OAuth2 시퀀스 다이어 그램
<img width="530" alt="스크린샷 2024-09-23 오후 1 19 48" src="https://github.com/user-attachments/assets/856b053b-88db-471d-a385-98e9a682db90">

## 👨‍👩‍👧‍👦 Developer
*  **한신** ([Urchinode](https://github.com/Urchinode))
*  **김영진** ([KmYgJn](https://github.com/KmYgJn))
