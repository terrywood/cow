import os
import requests
import json
import urllib
import time
import six

def __get_html( url):
    send_headers = {
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36',
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
        'Connection': 'keep-alive',
        'Host': 'xueqiu.com',
        'Cookie': r'xxxxxx',
    }

    if six.PY2:
        req = urllib2.Request(url, headers=send_headers)
        resp = urllib2.urlopen(req)
    else:
        req = urllib.request.Request(url, headers=send_headers)
        resp = urllib.request.urlopen(req)
    html = resp.read().decode('UTF-8')
    return html
    
def __get_portfolio_info(portfolio_code):
    """
    获取组合信息
    :return: 字典
    """
    url = "https://xueqiu.com/P/" + portfolio_code
    html = __get_html(url)
    pos_start = html.find('SNB.cubeInfo = ') + 15
    pos_end = html.find('SNB.cubePieData')
    json_data = html[pos_start:pos_end]
    portfolio_info = json.loads(json_data)
    print(portfolio_info)
    return portfolio_info 
    
    
def __virtual_to_balance( virtual):
    """
    虚拟净值转化为资金
    :param virtual: 雪球组合净值
    :return: 换算的资金
    """
    return virtual * 1000000

def get_balance():
    """
    获取账户资金状况
    :return:
    """
    portfolio_code = "ZH902949"  # 组合代码
    portfolio_info = __get_portfolio_info(portfolio_code)  # 组合信息
    asset_balance = __virtual_to_balance(float(portfolio_info['net_value']))  # 总资产
    position = portfolio_info['view_rebalancing']  # 仓位结构
    cash = asset_balance * float(position['cash']) / 100
    market = asset_balance - cash
    return [{'asset_balance': asset_balance,
             'current_balance': cash,
             'enable_balance': cash,
             'market_value': market,
             'money_type': u'人民币',
             'pre_interest': 0.25}]   

def __get_position():
    """
    获取雪球持仓
    :return:
    """
    portfolio_code = "ZH902949"  # 组合代码
    portfolio_info = __get_portfolio_info(portfolio_code)  # 组合信息
    position = portfolio_info['view_rebalancing']  # 仓位结构
    stocks = position['holdings']  # 持仓股票
    return stocks
            
def get_position():
    """
    获取持仓
    :return:
    """
    xq_positions = __get_position()
    balance = get_balance()[0]
    position_list = []
    for pos in xq_positions:
        volume = pos['weight'] * balance['asset_balance'] / 100
        position_list.append({'cost_price': volume / 100,
                              'current_amount': 100,
                              'enable_amount': 100,
                              'income_balance': 0,
                              'keep_cost_price': volume / 100,
                              'last_price': volume / 100,
                              'market_value': volume,
                              'position_str': 'xxxxxx',
                              'stock_code': pos['stock_symbol'],
                              'stock_name': pos['stock_name']
                              })
    return position_list            
result = get_position() 
print(result)           