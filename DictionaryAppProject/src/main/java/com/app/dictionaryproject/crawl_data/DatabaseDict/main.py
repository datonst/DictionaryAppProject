import mysql.connector
import re
import bs4
import mysql.connector
from getpass import getpass
import requests
from bs4 import BeautifulSoup
import asyncio,aiohttp
import certifi,ssl
import sys,random
#https://stackoverflow.com/questions/63347818/aiohttp-client-exceptions-clientconnectorerror-cannot-connect-to-host-stackover

agent = {
    'user-agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36'}




#https://oxylabs.io/resources/integrations/aiohttp

proxyList = [
    'http://222.255.27.180:80',
    '',
    '',
    '',
    
]
conn = aiohttp.TCPConnector(limit_per_host=5)
# def article_page(word):
#     word = word.replace(" ","_");
#     link = ''.join(["http://tratu.soha.vn/dict/en_vn/",word])
#     req=requests.get(url=link, headers=agent)
#     soup=BeautifulSoup(req.text,"html.parser")
#     prons=soup.select("#content-5>h5>span>b>font")
    
#     pronunciation = ''
#     Synonym = ''
#     Antonym = ''
#     for pron in prons :
#         if (pron != None) :
#             if (pronunciation =="") :
#                 pronunciation = ''.join(['/',pron.text,'/'])
#             else:
#                 pronunciation = ''.join([pronunciation,'\n','/',pron.text,'/'])

#     lists_element = soup.find_all(class_="section-h3")
#     for li in lists_element :
#         val = li.select_one("h3>span")
#         if val.text =="Từ đồng nghĩa" :
#             listOfSynonym= li.find(class_="section-h5").find_all("a"); #có thể find all ở đây nếu muốn tìm hết từ đồng nghĩa
#             i =0;
#             for e in listOfSynonym:
#                 if (Synonym == ""):
#                     Synonym = ''.join([e.text]);
#                 else :
#                     Synonym = ''.join([Synonym," ,",e.text]);
#                 i+=1;
#                 if(i>=5): break
#         elif val.text =="Từ trái nghĩa" :
#             listOfAntonym= li.find(class_="section-h5").find_all("a"); #có thể find all ở đây nếu muốn tìm hết từ đồng nghĩa
#             i =0;
#             for e in listOfAntonym:
#                 if (Antonym == ""):
#                     Antonym = ''.join([e.text]);
#                 else :
#                     Antonym = ''.join([Antonym," ,",e.text]);
#                 i+=1;
#                 if(i>=5): break
#     return [pronunciation,Synonym,Antonym]

mysql = mysql.connector.connect(
    user=input(), password=getpass(), host='localhost',port=3309,database='databasedict',
auth_plugin='mysql_native_password'
)

# ,proxy=random.choice(proxyList)


async def get_page(session, url,ssl_contexts):  
     
    async with session.get(url,timeout=100000,ssl_context= ssl_contexts) as r:
        # if r.status !=200:
        #     r.raise_for_status()
        return await r.text() # clone html in queue
    
async def get_all(session,list,ssl_contexts):
    tasks=[]
    for url in list:
        task= asyncio.create_task(get_page(session,url,ssl_contexts))
        tasks.append(task)
    results=await asyncio.gather(*tasks) # get list task ( clone all html page) in queue
    return results
async def root_data(list):
    ssl_contexts = ssl.create_default_context(cafile=certifi.where())
    session_timeout =   aiohttp.ClientTimeout(total=None,sock_connect=100000,sock_read=100000)
    async with aiohttp.ClientSession(timeout= session_timeout,headers=agent,trust_env=True) as session:
        data= await get_all(session,list,ssl_contexts)   #  
        return data

def parse(results):
    pronunciationList = []
    SynonymList = []
    AntonymList = []
    for html in results:
        soup=BeautifulSoup(html,"html.parser")
        prons=soup.select("#content-5>h5>span>b>font")
        pronunciation = ''
        Synonym = ''
        Antonym = ''
        for pron in prons :
            if (pron != None) :
                if pronunciation == "" :
                    pronunciation = ''.join([pron.text])
                else:
                    pronunciation = ''.join([pronunciation,'\n',pron.text])

        lists_element = soup.find_all(class_="section-h3")
        for li in lists_element :
            val = li.select_one("h3>span")
            if val.text =="Từ đồng nghĩa" :
                try:
                    listOfSynonym = li.find(class_="section-h5").find_all("a")
                except:
                    listOfSynonym= []
                    print(f"{pronunciation} : Synonym not found")
                i =0;
                for e in listOfSynonym:
                    if (Synonym == ""):
                        Synonym = ''.join([e.text]);
                    else :
                        Synonym = ''.join([Synonym," ,",e.text]);
                    i+=1;
                    if(i>=5): break
            elif val.text =="Từ trái nghĩa" :
                try:
                    listOfAntonym=li.find(class_="section-h5").find_all("a"); #có thể find all ở đây nếu muốn tìm hết từ đồng nghĩa
                except:
                    listOfAntonym =[]
                    print(f"{pronunciation} :Antonym not found")
                i =0;
                for e in listOfAntonym:
                    if (Antonym == ""):
                        Antonym = ''.join([e.text]);
                    else :
                        Antonym = ''.join([Antonym," ,",e.text]);
                    i+=1;
                    if(i>=5): break
        pronunciationList.append(pronunciation)
        SynonymList.append(Synonym)
        AntonymList.append(Antonym)
    print("SUCCESS")
    return [pronunciationList,SynonymList,AntonymList]
        # list_article =soup.find_all("div",class_="col-md-12 pl-0")
        # title=soup.find("li",class_="active").text.strip()[:-7]
        # date=soup.find("p", class_="published").text.strip()[16:].strip()
        # for article in list_article:
        #     id+=1
        #     name_article=article.find('a').text.strip()
        #     author_article=article.find("div",class_="meta").text.strip()
        #     # date= date_publish( article.find('a')["href"])
        #     list_result.append([id,title,name_article,author_article,date])  
    return 










mycursor= mysql.cursor();
class word_content:
    def __init__(self, word, pronunciation, wordtype, meaning,synonym, antonym):
        self.word = word
        self.pronunciation = pronunciation
        self.wordtype = wordtype
        self.meaning = meaning
        self.meaning = meaning
        self.synonym = synonym
        self.antonym = antonym
    def __str__(self):
        return f"{self.word} + {self.pronunciation} + {self.wordtype} + {self.meaning} + {self.synonym} + {self.antonym}"
def get(s):
    word = wordtype = meaning = ""
    pronunciation = ""
    for i in range(0,len(s)): 
        j = i
        if s[j]=='@':
            while s[j] != "/" and s[j] != '\n':
                if j != i: word += s[j]
                j += 1
            j -= 1
            continue

        if s[j]=='*':
            while s[j] != '\n':
                if j != i: wordtype += s[j]
                j += 1
            wordtype += '\n'
        i = j

    # data = article_page(word);
    pattern=r'\/.*?\/'
    match = re.search(pattern=pattern,string=s)


    if match:
        pronunciation=match.group(0)
        # print(pronunciation)
    # else:
        # pronunciation = data[0]

    for i in range(len(s)):
        if i > 0 and (s[i] == '-' or s[i] == '=') and s[i-1] == '\n':
            while i < len(s) and s[i] != '\n':
                if s[i] == '+':
                    meaning += ':'
                elif s[i] == '=':
                    meaning += '- '
                else: meaning += s[i]
                i += 1
            meaning += '\n'

    if len(wordtype) > 0 and wordtype[-1] == '\n': 
        wordtype = wordtype[:-1]
    return word_content(word,pronunciation,wordtype,meaning,"","")



def change(data) :
    data = data.strip()
    data = data.replace('\"', "`")
    data = data.replace('\'', "`")
    data = data.replace(';', ",")
    return data

listdata = []
linkList = []
def run_txt() :
    global listdata,linkList
    wordlist = []
    cur = ""
    with open("test.txt",mode="r",encoding="utf8") as f:
        for line in f:
            for x in line:
                if x == '@': 
                    if cur != "":
                        wordlist.append(cur)
                        cur = ""
                cur += x
            
    if cur != "":
        wordlist.append(cur)


    for word in wordlist:
        o = get(word)
    
        o.word = change(o.word)
        gt = o.word.replace(" ","_")
        linkList.append(''.join(["http://tratu.soha.vn/dict/en_vn/",gt]))
        # if (len(linkList)>100): break
        o.meaning = change(o.meaning)
        o.wordtype = change(o.wordtype)
        o.pronunciation = change(o.pronunciation)
        listdata.append(o)
        # createElement(id,o.word,o.pronunciation,o.wordtype,o.meaning,o.synonym,o.antonym)
        # dict_data.append([id,o.word,o.pronunciation,o.wordtype,o.meaning,o.synonym,o.antonym])
        # id += 1
    print("DONE")
    return listdata




def createTable():
    mycursor.execute("CREATE TABLE dict (id int NOT NULL AUTO_INCREMENT,word varchar(1000) NOT NULL, pronunciation varchar(1000), wordtype varchar(1000), meaning TEXT ,synonym varchar(1000),antonym varchar(1000),PRIMARY KEY (id));")


def createElement(id,word,pronunciation,wordtype,meaning,synonym, antonym,f_data) :
    try:
        f_data.write(f'INSERT INTO databasedict.dict VALUE ({int(id)},"{word}","{pronunciation}","{wordtype}","{meaning}","{synonym}","{antonym}");\n')
        mycursor.execute(f'INSERT INTO databasedict.dict VALUE ({int(id)},"{word}","{pronunciation}","{wordtype}","{meaning}","{synonym}","{antonym}");')
    except:
        print (f"{word} ---None")# mysql.commit()
# # dict_data = []

def done():
    mysql.commit()

run_txt()
# createTable()
policy = asyncio.WindowsSelectorEventLoopPolicy()
asyncio.set_event_loop_policy(policy)
results = asyncio.run(root_data(linkList))
# # for val in crawl_data:
# #     print(val)
# print(crawl_data[2])
crawl_data=parse(results)
def xu_ly():
    global crawl_data,listdata
    id =0
    if (len(crawl_data) ==0): return 
    pronunciationList = crawl_data[0];
    SynonymList = crawl_data[1]
    AntonymList = crawl_data[2]
    with open("D:/datas.txt", "w", encoding="utf-8") as f_data:
        for o in listdata:
            try:
                if(o.pronunciation == ""):
                    o.pronunciation = pronunciationList[id]
                    o.pronunciation = change(o.pronunciation)
                if (o.pronunciation == ""):
                    o.pronunciation = "Không có phiên âm"
                o.synonym = SynonymList[id]
                o.synonym = change(o.synonym)
                if (o.synonym == ""):
                    o.synonym ="Không có từ đồng nghĩa"
                o.antonym = AntonymList[id]
                o.antonym = change(o.synonym)
                if (o.antonym == ""):
                    o.antonym ="Không có từ trái nghĩa"
            except:
                print(f"NONE have")
            id += 1
            createElement(id,o.word,o.pronunciation,o.wordtype,o.meaning,o.synonym,o.antonym,f_data)
        f_data.close()
xu_ly()
done()








