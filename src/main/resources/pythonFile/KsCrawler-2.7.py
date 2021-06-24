# _*_ coding:utf-8 _*_
import sys
reload(sys)
sys.setdefaultencoding("utf-8")
import requests
import ast

# url = 'https://live.kuaishou.com/rest/wd/live/liveStream/myfollow'
# headers = {
#     'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36',
#     'Cookie': 'kpn=GAME_ZONE; kuaishou.live.web_st=ChRrdWFpc2hvdS5saXZlLndlYi5zdBKgAfB4ePIvI1gvQN1OJIc9_JiPXGmD6jlxv2UtH7f4YzS2MWpISxwFRTpuOFX8QHFEloAwKH5-HmalniRCQ1wraDgmPzfRIWSlmPpFZMqYBv5Dauu-LJCRuENAb1DYfFhDCXMrQOf9uZbHqECuFacPAyRcKnL6she__-pMwYbrOqF3SIf2Bh8ddWQrs9U_cRoUp-_5jnn5sxyWWnjdN7UJBN0aEhq2DZZ7DUHSmXUzdRyAf3O3USIgxoDIjeyM_XnnvWwSuGYOrYJJQRJFARn9PQ82CQKluwwoBTAB; kuaishou.live.web_ph=8d2c394e2f6d9cee0e708196cd89a1730baf; userId=2296616163; clientid=3; did=web_906215bd0e2b75a2a8d9f656ec28b547; userId=2296616163; Hm_lvt_86a27b7db2c5c0ae37fee4a8a35033ee=1622027002; Hm_lpvt_86a27b7db2c5c0ae37fee4a8a35033ee=1622027002; kuaishou.live.bfb1s=9b8f70844293bed778aade6e0a8f9942; client_key=65890b29'
# }
session = requests.Session()
def get_ks_html(headers,url):
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

resp = get_ks_html(ast.literal_eval(sys.argv[1]),sys.argv[2])
print(resp)
