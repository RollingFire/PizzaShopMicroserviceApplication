import requests
import json
import time
from datetime import datetime

CONFIG_SEPARATOR = "="
config = {}
baseURL = ""

class AbortTest(Exception):
    def __init__(self, message):
        self.message = message
    def __str__(self):
        return self.message

def loadConfig():
    with open('CustomerIntegrationTest.properties') as file:
        for line in file:
            if CONFIG_SEPARATOR in line:
                name, val = line.split(CONFIG_SEPARATOR, 1)
                config[name] = val.rstrip()

def checkDatabaseInitial():
    try:
        response = requests.get(baseURL + "/customer")
        if response.status_code == 200:
            customers = response.json()
            if customers == []:
                firstName = "Paul"
                lastName = "Blart"
                queryPerams = "?firstName=" + firstName + "&lastName=" + lastName
                response = requests.post(baseURL + "/customer/" + queryPerams)
                if response.status_code == 201:
                    response = requests.get(baseURL + "/customer/1")
                    if response.status_code == 200:
                        customer = response.json()
                        if customer["firstName"] == firstName and customer["lastName"] == lastName:
                            response = requests.delete(baseURL + "/customer/1")
                            if response.status_code == 200:
                                response = requests.get(baseURL + "/customer")
                                if response.status_code == 200:
                                    customers = response.json()
                                    if customers == []:
                                        print("TEST PASSED - checkDatabaseInitial")
                                        return
            raise AbortTest("TEST ABORTED - Data base was not empty at the start of integration testing. Tests will fail even if working correctly. Start new container of database and run again.")
        raise AbortTest("TEST FAILED - checkDatabaseInitial: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - checkDatabaseInitial: Error - " + str(e))

def main():
    pass

if __name__ == "__main__":
    #Extencivly test getCustomersByName and /customer get
    try:
        loadConfig()
        baseURL = "http://" + config['customerAPI.host'] + ":" + config['customerAPI.port']
        try:
            checkDatabaseInitial()

        except AbortTest as err:
            print(err)
    except (KeyboardInterrupt, SystemExit) as e:
        raise e