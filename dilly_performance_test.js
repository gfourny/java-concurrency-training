import http from 'k6/http';
import { check } from 'k6';

export let options = {
    vus: 10, // nombre d'utilisateurs virtuels
    duration: '10s', // durée du test
};

export default function() {
  let response = http.get('http://localhost:8080/api/dilly');
  
  check(response, {
      'http response status code is 200': (response) => response.status === 200,
  });
};