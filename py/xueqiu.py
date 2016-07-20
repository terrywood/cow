# -*- coding: utf-8 -*-
"""
Created on Mon Jul 18 18:36:34 2016

@author: Administrator
"""

import requests
import hashlib
import re
import time
from datetime import datetime
# 构造 Request headers
agent = 'Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36'
headers = {
    'User-Agent': agent,
    'Host': "xueqiu.com",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate, sdch, br",
    "Accept-Language": "zh-CN,zh;q=0.8,zh-TW;q=0.6",
    "Connection": "keep-alive"
}
telephone = "13660288080"
password = "abcabc123123"
session = requests.session()
totalCount =0
loginTimes=0

# 密码的 md5 加密
def get_md5(password):
    md5 = hashlib.md5()
    md5.update(password.encode())
    return md5.hexdigest().upper()
def order(code,price,prev_weight_adjusted,target_weight):

    pass

def handleData(json):
    #print(json)
    now = datetime.now().timestamp()
    for obj in json['list']:
        if(obj['status'] == "success" or obj['status'] == "pending" ):
            diffMin = (now -(obj['updated_at']/1000))/60/60
            if(diffMin<30):
                entity = obj['rebalancing_histories'][0]
                order(entity['stock_id'],entity['price'],entity['prev_weight_adjusted'],entity['target_weight'])

            #print(datetime.fromtimestamp(t))
            #print(updateat/60/60)


def heartbeat():
    x=0
    global totalCount
    while True:
        x=x+1
        r = session.get("https://xueqiu.com/cubes/rebalancing/history.json?cube_symbol=ZH902949&count=10&page=1",headers=headers)
        aa = r.json()
        try:
            _totalCount = aa['totalCount']
            if totalCount!=_totalCount:
                print("call change API -----")
                totalCount=_totalCount
                handleData(aa)
            else:
                print("no changeed -------------")
        except:
            print("error-------------------")
            if(loginTimes>=10):
                break
            else:
                login()

        time.sleep(5)
        if x>10:
            break

    print("finish")


# 只写了手机号登录的情况，邮箱登录的情况，可以简单修改 postdata 后实现、
def login():
    url = 'https://xueqiu.com/'
    session.get(url, headers=headers)  # 访问首页产生 cookies
    headers['Referer'] = "https://xueqiu.com/"
    login_url_api = "https://xueqiu.com/service/csrf?api=%2Fuser%2Flogin"  # 模拟更真实的请求
    session.get(login_url_api, headers=headers)
    login_url = "https://xueqiu.com/user/login"
    postdata = {
        "areacode": "86",
        "password": get_md5(password),
        "remember_me": "on",
        "telephone": telephone
    }
    log = session.post(login_url, data=postdata, headers=headers)
    log = session.get("https://xueqiu.com/setting/user", headers=headers)
    pa = r'"profile":"/(.*?)","screen_name":"(.*?)"'
    res = re.findall(pa, log.text)
    global loginTimes
    if res == []:
        print("登录失败，请检查你的手机号和密码输入是否正确")
        loginTimes = loginTimes+1
    else:
        print('欢迎使用 xchaoinfo 写的模拟登录 \n 你的用户 id 是：%s, 你的用户名是：%s' % (res[0]))
        loginTimes =0
        heartbeat()


if __name__ == '__main__':
    login()# -*- coding: utf-8 -*-

