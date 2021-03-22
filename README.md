# hanium
### 2020 hanium project - Intelligent Food Ingredients Management X E-Commerce  
  
# 소개  
### 음식물 쓰레기 문제 해결을 위한 식재료 지능화 관리
  * 식재료 구매부터 폐기까지 정보 및 기능 제공(검색, 챗봇)
  * 식재료 보관기한 임박 알림(푸시 알림)
  * 레시피 추천 알고리즘(협업 필터링)
### 보통의 냉장고를 스마트 냉장고화
  * 국내 스마트 냉장고의 기술을 갖고 있는 앱 서비스
  * IOT 장비를 이용한 냉장고 원격 관리

# 성과  
### 한이음 공모전 입선 
![image](https://user-images.githubusercontent.com/67588446/111149024-b0734180-85cf-11eb-9d20-bc86fe457f88.png)  
시연(youtube) : https://youtu.be/hYwN-_jjMM4
### 한이음 학술대회 참가
발표(youtube) : https://youtu.be/5zSMExgf2hM  
### KIPS 2020 온라인 추계학술발표대회 논문게재
논문 : https://manuscriptlink-society-file.s3-ap-northeast-1.amazonaws.com/kips/conference/2020fall/presentation/KIPS_C2020B0129.pdf  

# 요구사항
<details>
  <summary>상세 내용 확인</summary>
  <div markdown="1">
    
### S/W
| 기능 | 설명 |
| --------------- | -------------------------------------------------- |
|보관기한 확인|보관기한 임박한 식재료의 목록을 확인|
|보관기한 임박 알림|보관기한 임박한 식재료 푸시 알림|
|장바구니에 식재료 추가|이커머스 사이트에 검색 및 구매 링크 연결|
|레시피 CRUD|레시피 등록, 검색, 수정, 삭제|
|레시피 추천|식재료 보관기한과 추천 알고리즘에 기반한 레시피 추천|
|식재료 검색|사용자가 원하는 식재료 검색|
|식재료 정보|사용자가 원하는 식재료의 정보 제공|
|AI 챗봇|사용자가 원하는 질문을 입력하면 그에 맞는 응답을 제공하는 챗봇|
|회원정보 관리|회원가입, 로그인, 회원정보 수정, 알림 설정|

### H/W
| 기능 | 설명 |
| --------------- | -------------------------------------------------- |
|냉장고 내부 온도 측정|온도 센서를 통해 냉장고 내부 온도 측정|
|냉장고 내부 습도 측정|습도 센서를 통해 냉장고 내부 습도 측정|
|냉장고 내부 확인|카메라 모듈을 통해 냉장고 내부 확인|
|웹 서버 연결|웹 서버와 연동하여 통신하고, 각 센서로부터 측정 값을 수신|

  </div>
</details>

# 설계
<details>
  <summary>상세 내용 확인</summary>
  <div markdown="1">
    
### 1. S/W
![image](https://user-images.githubusercontent.com/67588446/111156860-8de62600-85d9-11eb-8c2c-d820330bb290.png)
### 2. H/W
![image](https://user-images.githubusercontent.com/67588446/111154512-a4d74900-85d6-11eb-9773-dd0cada70bfb.png)
### 3. 서비스흐름도
![image](https://user-images.githubusercontent.com/67588446/111154553-ae60b100-85d6-11eb-968f-a947ceb3ea2c.png)
### 4. ERD
![image](https://user-images.githubusercontent.com/67588446/111985595-24719480-8b50-11eb-8958-df8a0c61104a.png)

  </div>
</details>

# 개발환경
<details>
  <summary>상세 내용 확인</summary>
  <div markdown="1">
       
### BE
| 구분 | 개발환경 | 개발도구 | 개발언어 |
| ----- | --------- | --------- | --------- |
|Server|VSCode|Node.js|JavaScript|
|DB|HeidiSQL|MySQL|SQL|
|\*CF|Pycharm|-|Python|

\*CF : Collaborative Filtering  
  
### INFRA-AWS
| 구분 | 서비스 |
| --------- | ------------- |
|Computing|EC2 CentOS7|
|Storage|S3|
|CDN|CloudFront|
  
### FE
|구분|개발환경|개발도구|개발언어|
|--|--| -- |--|
|Front|Android Studio| - | Java |
  
  </div>
</details>


# API DOCS
<details>
  <summary>상세 내용 확인</summary>
  <div markdown="1">
    
* USER
  * [[POST] 회원가입](https://github.com/owenyi/hanium/wiki/%5BPOST%5D-회원가입)
  * [[POST] 로그인](https://github.com/owenyi/hanium/wiki/%5BPOST%5D-로그인)

* INGREDIENTS
  * [[GET] 식재료](https://github.com/owenyi/hanium/wiki/%5BGET%5D-식재료)
  * [[GET] 식재료상세](https://github.com/owenyi/hanium/wiki/%5BGET%5D-식재료상세)
  * [[GET] 식재료검색](https://github.com/owenyi/hanium/wiki/%5BGET%5D-식재료검색)
  * [[POST] 냉장고넣기](https://github.com/owenyi/hanium/wiki/%5BPOST%5D-냉장고넣기)
  * [[POST] 장바구니넣기](https://github.com/owenyi/hanium/wiki/%5BPOST%5D-장바구니넣기)

* RECIPE
  * [[GET] 레시피](https://github.com/owenyi/hanium/wiki/%5BGET%5D-레시피)
  * [[GET] 레시피상세](https://github.com/owenyi/hanium/wiki/%5BGET%5D-레시피상세)
  * [[GET] 추천레시피](https://github.com/owenyi/hanium/wiki/%5BGET%5D-추천레시피)
  * [[POST] 레시피작성](https://github.com/owenyi/hanium/wiki/%5BPOST%5D-레시피작성)
  * [[GET] 레시피검색](https://github.com/owenyi/hanium/wiki/%5BGET%5D-레시피검색)
  * [[GET] rating](https://github.com/owenyi/hanium/wiki/%5BGET%5D-rating)
  * [[POST] rating](https://github.com/owenyi/hanium/wiki/%5BPOST%5D-rating)
  * [[POST] 레시피에서장바구니](https://github.com/owenyi/hanium/wiki/%5BPOST%5D-레시피에서장바구니)

* REFRIGERATOR
  * [[GET] 냉장고](https://github.com/owenyi/hanium/wiki/%5BGET%5D-냉장고)
  * [[GET] 임박식재료](https://github.com/owenyi/hanium/wiki/%5BGET%5D-임박식재료)
* BASKET
  * [[GET] 장바구니](https://github.com/owenyi/hanium/wiki/%5BGET%5D-장바구니)
  * [[POST] 장바구니에서냉장고](https://github.com/owenyi/hanium/wiki/%5BPOST%5D-장바구니에서냉장고)

* CRAWLING
  * [[GET] SSG](https://github.com/owenyi/hanium/wiki/%5BGET%5D-SSG)

* IOT
  * [[GET] 온습도](https://github.com/owenyi/hanium/wiki/%5BGET%5D-온습도)
  * [[GET] 냉장고내부](https://github.com/owenyi/hanium/wiki/%5BGET%5D-냉장고내부)

* CHATBOT
  * [[GET] 챗봇](https://github.com/owenyi/hanium/wiki/%5BGET%5D-챗봇)

  </div>
</details>
