import express from 'express';
import bodyParser from 'body-parser';
require('body-parser-xml')(bodyParser);
const app = express();

app.use(bodyParser.urlencoded({extended:true,limit:'5mb'}))
app.use(bodyParser.json())
app.use(bodyParser.text({type:'text/xml'}))
app.disable('x-powered-by')

app.use('/wx', require('./router/wx'))
app.use('/webhook', require('./router/webhook'))
// 解决微信支付通知回调数据
app.use(bodyParser.xml({
  limit: '2MB',   // Reject payload bigger than 1 MB
  xmlParseOptions: {
    normalize: true,     // Trim whitespace inside text nodes
    normalizeTags: true, // Transform tags to lowercase
    explicitArray: false // Only put nodes in array if >1
  }
}));
app.engine('.html', require('ejs').__express);
app.set('view engine', 'html');
app.set('views', __dirname + '/views');
app.listen(9064);
