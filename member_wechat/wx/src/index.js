import express from 'express';
import bodyParser from 'body-parser';
var log4js = require("./log");
const app = express();
app.use(log4js.useLog());//新增日志打印

app.use(bodyParser.urlencoded({extended:true,limit:'5mb'}))
app.use(bodyParser.json())
app.use(bodyParser.text({type:'text/xml'}))
app.disable('x-powered-by')

app.use('/wx', require('./router/wx'))
app.use('/webhook', require('./router/webhook'))

app.listen(9064);
