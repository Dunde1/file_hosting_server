# 파일 호스팅 서버
+ 현재 이미지 파일 호스팅 서버의 기능만 담당하는 중
### 설명
+ 외부에서 파일을 전송받아 내부에 저장하고 DB에 경로를 저장합니다.
+ DB에 저장된 경로를 통해 파일을 외부에서 받을 수 있습니다.
### 사용법
+ 현재 이미지만 파일 호스팅이 가능합니다.
#### 사진넣기
+ 주소 : http://내부아이피:8888/requestSave
+ 전송방식 : Post
+ 필요한 요소 (파일의 이름과 속성은 동일하게 주어야 합니다.)
  + 제목 : String imgTitle
  + 요청자 : String requester
  + 파일 : MultipartFile imgFile
+ 리턴
  + ResponseBody 방식으로 String 타입 리턴
  + "OK" : 전송이 성공적으로 완료됨.
  + "No_title" : 제목없음.
  + "No_requester_name" : 작성자 없음.
  + "No_support_extension" : 확장자가 jpg, jpeg, png 가 아님.
  + "file_conversion_error" : 파일 변환 실패. 개발자에게 문의 바랍니다.

#### 사진꺼내기
+ 유저사진 저장주소 : http://내부아이피:8888/user/이미지이름.확장자
  + 관리자 승인 전 사진이 모이는 곳
+ 모듈사진 저장주소 : http://내부아이피:8888/module/이미지이름.확장자
  + 관리자 승인 후 사진이 모이는 곳

#### 관리자 승인
+ 주소 : http://내부아이피:8888/requestAccept
+ 전송방식 : Post
+ 필요한 요소 (파일의 이름과 속성은 동일하게 주어야 합니다.)
  + 관리자 키 : String key (키는 비공개)
  + 이미지 넘버(DB) : Long imgNum
+ 리턴
  + ResponseBody 방식으로 String 타입 리턴
  + "OK" : 성공적으로 완료됨.
  + "Not_matched_key" : 관리자 키가 맞지 않음.
  + "No_image_id" : DB서버에 해당 이미지 id가 존재하지 않음.
  + "Redundant_work" : 이미 승인된 이미지 파일.
  + "file_conversion_error" : 파일 변환 실패. 개발자에게 문의 바랍니다.

