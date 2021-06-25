import requests
import io
import sys
import ast

sys.stdout = io.TextIOWrapper(sys.stdout.buffer,encoding='utf8') #改变标准输出的默认编码
session = requests.Session()

def ks_crawler_kit(headers,url):
    try:
        response = session.get(url=url, headers=headers)
    except  BaseException as be:
        print(be)
    else:
        if (response.status_code == 200):
            response.encoding = 'utf-8'
            return response.text
        else:
            response.encoding = 'utf-8'
            print(response.text)
            return response.status_code
#调用函数
resp = ks_crawler_kit(ast.literal_eval(sys.argv[1]),sys.argv[2])
print(resp)