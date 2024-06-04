import { reactive } from 'vue'

export const checkPrice = (rule: any, value: any, callback: any) => {
  if (!value) {
    return callback(new Error('상품의 가격을 입력해주세요.'))
  }

  if (!Number.isInteger(value)) {
    callback(new Error('가격은 숫자로 입력해주세요.'))
  } else {
    if (value < 1000) {
      callback(new Error('가격은 1000원 보다 큰 값을 입력해주세요.'))
    } else {
      callback()
    }
  }
}

export const createFormRules = () =>
  reactive({
    itemName: [
      { required: true, message: '상품명을 입력해주세요.', trigger: 'blur' },
      { min: 2, max: 30, message: '상품명은 2~30자로 입력해주세요.', trigger: 'blur' },
    ],
    price: [{ required: true, validator: checkPrice, trigger: 'blur' }],
    stock: [{ required: true, message: '상품의 재고 수를 입력하세요.', trigger: 'blur' }],
  })
