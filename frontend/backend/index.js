const express = require('express');
const cors = require('cors');
const axios = require('axios');
const app = express();
app.use(cors());


app.get('/pessoa/login', (req, res) => {
  const resposta = axios.get('https://apicivitas.herokuapp.com', {
    auth: {
      username: 'john.the.killer@gmail.com',
      password: 'andy123'
    }
  })

  res.status(200).json(resposta);
})

app.listen(3000, () => console.log('listening on 3000'))
