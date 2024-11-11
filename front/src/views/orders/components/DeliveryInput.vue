<template>
  <el-divider />
  <div class="orderer-container">
    <div class="orderer-container__left">
      <strong>주문자</strong>
    </div>
    <div class="orderer-container__right">
      <span> {{ dummy_nickname }} | {{ dummy_email }} </span>
    </div>
  </div>
  <el-divider />

  <el-tabs v-model="activeTab" type="card">
    <el-tab-pane label="기존 배송 정보" name="existing">
      <div class="delivery-info-container">
        <ul>
          <li>
            <div class="input-row">
              <label class="label">수령인</label>
              <div class="input">
                <el-input v-model="deliveryInfo.receiver" placeholder="이름을 입력하세요" />
              </div>
            </div>
          </li>
          <!-- 배송 주소 -->
          <li>
            <div class="input-row">
              <div class="input">
                <div class="zipcode-input">
                  <el-input :value="deliveryInfo.zonecode" style="width: 25%" disabled />
                  <el-button type="primary" @click="postalCodeModalVisible()" style="margin-left: 10px">
                    주소 찾기
                  </el-button>
                </div>
              </div>
            </div>
          </li>
          <li>
            <div class="input-row">
              <label class="label">도로명 주소</label>
              <div class="input">
                <el-input :value="deliveryInfo.roadAddress" disabled />
              </div>
            </div>
          </li>
          <li>
            <div class="input-row">
              <label class="label">지번 주소</label>
              <div class="input">
                <el-input :value="deliveryInfo.jibunAddress" disabled />
              </div>
            </div>
          </li>
          <li>
            <div class="input-row">
              <label class="label">상세 주소</label>
              <div class="input">
                <el-input v-model="deliveryInfo.detailAddress" />
              </div>
            </div>
          </li>
          <!-- 전화 번호 -->
          <li>
            <div class="input-row">
              <label class="label">전화 번호</label>
              <div class="input">
                <el-input v-model="deliveryInfo.phoneNumber" placeholder="하이픈(-)을 빼고 번호만 입력해주세요" />
              </div>
            </div>
          </li>
        </ul>
      </div>
    </el-tab-pane>
    <!-- <el-tab-pane label="직접 입력" name="direct">
      <div>
        <el-button type="primary" @click="postalCodeModalVisible()">우편번호 검색</el-button>
        <el-dialog title="우편번호 검색" v-model:visible="modalVisible">
          <div id="daum_postcode"></div>
        </el-dialog>
      </div>
    </el-tab-pane> -->
  </el-tabs>
</template>

<script setup lang="ts">
import { ref } from 'vue'

declare global {
  interface Window {
    daum: any
  }
}

const activeTab = ref('existing')
const dummy_nickname = '간술맨'
const dummy_email = 'tipsyboy@gmail.com'

const deliveryInfo = ref({
  zonecode: '12345',
  receiver: '간술맨',
  phoneNumber: '01012345678',
  roadAddress: '경기 성남시 분당구 판교역로 166',
  jibunAddress: '경기 성남시 분당구 백현동 532',
  deliveryNote: '부재 시 경비실에 맡겨주세요.',
  detailAddress: '102호',
})

const modalVisible = ref(false) // 모달 창의 가시성을 관리하기 위한 ref 변수
const postalCodeModalVisible = () => {
  modalVisible.value = true // 우편번호 검색 버튼을 클릭하면 모달 창을 엽니다.

  const script = document.createElement('script')
  script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js'
  script.onload = () => {
    new window.daum.Postcode({
      oncomplete: function (data: any) {
        var roadAddr = data.roadAddress // 도로명 주소
        var jibunAddr = data.jibunAddress
        var extraRoadAddr = '' // 참고 항목 (동, 건물 이름 등)

        // 도로명 주소 만들기
        if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
          extraRoadAddr += data.bname
        }
        if (data.buildingName !== '' && data.apartment === 'Y') {
          extraRoadAddr += extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName
        }
        if (extraRoadAddr !== '') {
          extraRoadAddr = ' (' + extraRoadAddr + ')'
        }

        if (jibunAddr === '' && data.autoJibunAddress !== '') {
          jibunAddr = data.autoJibunAddress
        }

        if (extraRoadAddr !== '') {
          roadAddr += extraRoadAddr
        }

        deliveryInfo.value.zonecode = data.zonecode
        deliveryInfo.value.roadAddress = roadAddr
        deliveryInfo.value.jibunAddress = jibunAddr

        modalVisible.value = false
      },
    }).open()
  }
  document.head.appendChild(script)
}
</script>

<style scoped>
.orderer-container {
  display: flex;
  justify-content: space-around;
}

.delivery-info-container ul {
  list-style: none;
}

.address-container {
  display: flex;
  flex-direction: column;
}

.zipcode-input {
  display: flex;
  /* justify-content: flex-start; */
  align-items: center;
}

.input-row {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.label {
  flex: 0 0 100px; /* 라벨의 폭을 조정합니다. */
}

.input {
  flex: 1; /* 입력 요소가 남은 공간을 채우도록 설정합니다. */
}
</style>
