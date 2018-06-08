cd G:/github/zjmembers/member_wechat
# cd /f/node/zjmembers/member_wechat

date > git.log
git pull >> git.log

cat git.log | grep '^ wx/package.json' && cd wx && npm install --registry=https://registry.npm.taobao.org && cd ..

cat git.log | grep '^ wx' && cd wx && npm run build > build.log && pm2 reload wx && cd ..

cat git.log | grep '^ elec_member_wechat/package.json' && cd elec_member_wechat && npm install --registry=https://registry.npm.taobao.org && cd ..

cat git.log | grep '^ elec_member_wechat' && cd elec_member_wechat && mv dist dist_old && npm run build > build.log && rm -rf dist_old && cd ..

cat git.log | grep '^ nginx2.conf' && nginx -s reload
