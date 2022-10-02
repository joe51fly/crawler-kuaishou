import requests
import io
import sys
import ast
from requests.packages.urllib3.exceptions import InsecureRequestWarning

# 禁用安全请求警告
requests.packages.urllib3.disable_warnings(InsecureRequestWarning)

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf8')  # 改变标准输出的默认编码
session = requests.Session()


def ks_short_video_crawler_kit(short_video_headers, my_like_payload, short_video_url):
    try:
        response = session.post(url=short_video_url, json=my_like_payload, headers=short_video_headers, verify=False)
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


# print(ast.literal_eval(sys.argv[1]))
# print(sys.argv[1])
# 调用函数
resp = ks_short_video_crawler_kit(ast.literal_eval(sys.argv[1]), ast.literal_eval(sys.argv[2]), sys.argv[3])
print(resp)
