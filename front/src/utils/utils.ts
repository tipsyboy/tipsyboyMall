export const formatCurrency = (price: number): string => {
  if (!price) return '0'
  return new Intl.NumberFormat('ko-KR').format(price)
}
