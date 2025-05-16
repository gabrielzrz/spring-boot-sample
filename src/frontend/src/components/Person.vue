<template>
    <div class="container">
        <h1>People</h1>
        <table class = "table table-striped">
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Address</th>
                    <th>Gender</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="person in people" v-bind:key="person.id">
                    <td> {{person.id}}</td>
                    <td> {{person.name}}</td>
                    <td> {{person.address}}</td>
                    <td> {{person.gender}}</td>
                </tr>
            </tbody>
        </table>
        <div class="pagination">
            <button
                :disabled="page.number === 0"
                @click="getPeople(page.number - 1)">
                Anterior
            </button>

            <span>Página {{ page.number + 1 }} de {{ page.totalPages }}</span>

            <button
                :disabled="page.number + 1 >= page.totalPages"
                @click="getPeople(page.number + 1)">
                Próxima
            </button>
        </div>
    </div>
</template>

<script>
import PersonService from '../services/PersonService';

    export default {
        name: 'PersonList',
        data(){
            return {
                people: [],
                page: {
                    size: 0,
                    totalElements: 0,
                    totalPages: 0,
                    number: 0
                }
            }
        },
        methods: {
            getPeople(page = 0){
                PersonService.getPeople(page).then((response) => {
                console.log(response.data);
                    this.people = response.data._embedded.people
                    this.page = response.data.page;
                });
            }
        },
        created() {
            this.getPeople();
        }
    }
</script>
