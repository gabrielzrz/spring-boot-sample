import axios from 'axios'

const PERSON_API_BASE_URL = '/zorzi/api/person'

class PersonService {
    getPeople(page = 0, size = 10){
        return axios.get(`${PERSON_API_BASE_URL}?page=${page}&size=${size}&direction=desc`);
    }
}

export default new PersonService()