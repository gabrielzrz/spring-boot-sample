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
                    <th>Birth day</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="person in people" v-bind:key="person.id">
                    <td> {{person.id}}</td>
                    <td> {{person.name}}</td>
                    <td> {{person.address}}</td>
                    <td> {{person.gender}}</td>
                    <td> {{person.birthDay}}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import PersonService from '../services/PersonService';

    export default {
        name: 'PersonList',
        data(){
            return {
                people: []
            }
        },
        methods: {
            getPeople(){
                PersonService.getPeople().then((response) => {
                    this.people = response.data;
                });
            }
        },
        created() {
            this.getPeople();
        }
    }
</script>
