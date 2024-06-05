import ItemCreate from '@/views/item/ItemCreate.vue'
import ItemDetail from '@/views/item/ItemDetail.vue'
import ItemEdit from '@/views/item/ItemEdit.vue'
import ItemList from '@/views/item/ItemList.vue'

export default [
  {
    path: '/item/save',
    name: 'itemCreate',
    component: ItemCreate,
  },
  {
    path: '/item/:itemId',
    name: 'itemDetail',
    component: ItemDetail,
    props: true,
  },
  {
    path: '/item/edit/:itemId',
    name: 'itemEdit',
    component: ItemEdit,
    props: true,
  },
  {
    path: `/items`,
    name: `itemList`,
    component: ItemList,
    props: true,
  },
]
