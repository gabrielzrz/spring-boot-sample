import axios from 'axios'

const PERSON_API_BASE_URL = '/zorzi/api/person'

class PersonService {

    getPeople(){
        return axios.get(PERSON_API_BASE_URL);
    }
}

export default new PersonService()