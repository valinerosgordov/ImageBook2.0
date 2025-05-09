<template>
  <b-container>
    <b-row>
      <b-col>
        <h1>Калька</h1>
        <b-table hover :items="vellums" :fields="fields" class="mt-4">
          <template slot="active" slot-scope="data">
            <span v-if="data.value">v</span>
          </template>
          <template slot="edit" slot-scope="row">
            <b-button @click="row.toggleDetails">
              {{ row.detailsShowing ? 'Закрыть' : 'Редактировать'}}
            </b-button>
          </template>
          <template slot="remove" slot-scope="row">
            <b-button @click="remove(row.item)" variant="danger">Удалить</b-button>
          </template>
          <template slot="row-details" slot-scope="row">
            <b-card>
              <b-form>
                <b-form-group horizontal label="Активный">
                  <b-form-checkbox v-model="row.item.active"/>
                </b-form-group>
                <b-form-group horizontal label="Номер">
                  <b-form-input v-model="row.item.number" type="number"/>
                </b-form-group>
                <b-form-group horizontal label="Техническое название">
                  <b-form-input v-model="row.item.innerName" type="text"/>
                </b-form-group>
                <b-form-group horizontal label="Название">
                  <b-form-input v-model="row.item.name" type="text"/>
                </b-form-group>
                <b-form-group horizontal label="RGB-цвет #">
                  <b-form-input v-model="row.item.colorRGB" type="text"/>
                </b-form-group>
                <b-form-group horizontal label="Изображение для ЛК">
                  <b-form-file v-model="row.item.appImageFile" :placeholder="row.item.appImageFilename || 'Выберите файл...'"></b-form-file>
                </b-form-group>
                <b-form-group horizontal label="Стоимость кальки A5 x 2 стороны, руб.">
                  <b-form-input v-model="row.item.price" type="number"/>
                </b-form-group>
                <b-form-group horizontal label="Стоимость для типографии, руб.">
                  <b-form-input v-model="row.item.phPrice" type="number"/>
                </b-form-group>

                <b-button @click="save(row.item)">Сохранить</b-button>
                <b-button @click="row.toggleDetails; load()">Отменить</b-button>
              </b-form>
            </b-card>
          </template>
        </b-table>
        <b-button @click="add" variant="primary">Добавить</b-button>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
  import api from '@/services/api'

  export default {
    name: "Vellums",

    data() {
      return {
        vellums: [],
        fields: [
          {
            key: 'active',
            label: 'Активный',
            'class': 'text-center'
          },
          {
            key: 'number',
            label: 'Номер'
          },
          {
            key: 'innerName',
            label: 'Техн. название'
          },
          {
            key: 'colorRGB',
            label: 'RGB-цвет'
          },
          {
            key: 'edit',
            label: ''
          },
          {
            key: 'remove',
            label: ''
          },
        ],
        params: {
          vellumPrice: 0,
          vellumPhPrice: 0
        }
      }
    },

    mounted() {
      this.load()
    },

    methods: {
      async load() {
        this.vellums = await api.get(`/vellums/`)
      },
      async add() {
        await api.post(`/vellums/add`)
        this.load()
      },
      async save(vellum) {
        await api.postMultipart(`/vellums/${vellum.id}/update`, { vellum }, { appImageFile: vellum.appImageFile })
        this.load()
      },
      async remove(vellum) {
        await api.post(`/vellums/${vellum.id}/delete`)
        this.load()
      }
    }
  }
</script>

<style scoped>

</style>
