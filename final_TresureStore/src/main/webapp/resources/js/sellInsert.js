 /**
 * 
 */
 
 /* 상품 이미지 */
window.onload = function (){ 
	$('#inputImage').on('change', readURL); //파일 올릴 때마다 readURL 함수 호출
}
const fileBuffer = []; //파일저장용 전역변수

//이미지 미리 보기 함수
function readURL() {
	const maxSize = 10 * 1024 * 1024;//이미지 파일 당 최대 사이즈 설정 : 10MB
	const input = this;
	
	if(input.files && input.files[0]) { //크기가 정해지지 않은 files 배열
		var reader = new FileReader();
		
		//이미지 파일 개수 제한
		if($('.registUserImages').length + input.files.length > 5) {
			alert('사진은 최대 5장 까지 올릴 수 있습니다.');
			return false;
		}
		let index = 0;
		
		reader.onload = function(evt) {
			//이미지 파일 사이즈 비교
			const fileSize = input.files[index].size;
			if(fileSize > maxSize) {
				alert('이미지파일 사이즈는 10MB 이내로 등록 가능합니다.');
				return false;
			}
			const image = input.files[index];
			
			//이미지 파일 유효성 검사
			const fileEx = image.name.slice(image.name.lastIndexOf(".")+1).toLowerCase();
			if(fileEx != "jpg" && fileEx != "png" && fileEx != "gif" && fileEx != "bmp" && fileEx != "jpeg") {
				
				return false;
			}
			
			fileBuffer.push(image); //push
			
			//이미지 태그 생성
			const $li = $('<li></li>').attr({ draggable: 'false' }).addClass('registUserImages');
			const $div = $('<div></div>');
			const $image = $('<img/>').attr({ 'src': this.result, alt: '상품이미지' });
			const $button = $('<button></button>');

			//첫번째 이미지를 대표이미지로 설정
			if($('.registUserImages').length == 0){
				index = 0;
				$div.addClass('imageRepresentive').text('대표이미지');		
			}
			
			//태그 붙이기
			$('#imageList').append($li); //ul 태그의 하위 태그로 li 태그 붙이기
			$li.append($div, $image, $button); //li 태그의 하위 태그로 세가지 태그 붙이기
			
			//X버튼 클릭 시 deleteImage 함수 호출
			$button.attr('type', 'button').addClass('image_cancleBtn').click(deleteImage);
			
			//상품이미지(0/5) 개수 변경
			$('.image_sub small').text(`(${$('.registUserImages').length}/5)`);
			
			//한번에 이미지 여러개 넣기
			if(index < input.files.length - 1) {
				reader.readAsDataURL(input.files[++index]);
			}
		}
		//이미지를 데이터 URI로 표현
		reader.readAsDataURL(input.files[index]);
	}
}

//이미지 삭제 함수
function deleteImage() {
	//배열 내의 파일 제거
	const fileIndex = $(this).closest('li').index();
    fileBuffer.splice(fileIndex - 1, 1);
    
    //대표이미지를 지웠을 시 다음 이미지를 대표이미지로 지정
	if($(this).closest('li').children('div').hasClass('imageRepresentive')) {
		const $pre = $(this).closest('li').next().find('div').addClass('imageRepresentive').text('대표이미지');
	}
	$(this).closest('li').remove();
	$('.image_sub small').text(`(${$('.registUserImages').length}/5)`);
};
 