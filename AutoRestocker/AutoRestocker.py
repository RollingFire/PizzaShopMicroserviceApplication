import requests
import json
import time
from datetime import datetime

CONFIG_SEPARATOR = "="
config = {}
baseURL = ""


def loadConfig():
    with open('restocker.properties') as file:
        for line in file:
            if CONFIG_SEPARATOR in line:
                name, val = line.split(CONFIG_SEPARATOR, 1)
                config[name] = val.rstrip()


def restockItem(inventoryItem):
    if inventoryItem['units'] <= inventoryItem['restockAt'] and inventoryItem['restockAt'] > 0:
        request = {"unitsAdded": inventoryItem['restockAmount']}
        response = requests.post(baseURL + "/inventory/restock/" + str(inventoryItem['id']), json=request)
        if response.status_code != 200:
            print(str(datetime.now()) + " Response status code from a post call to /inventory/restock/id: " + str(response.status_code))
        else:
            print(str(datetime.now()) + " Restocked " + str(inventoryItem['name']) + " with " + str(inventoryItem['restockAmount']) + " " + str(inventoryItem['unitType']))


def restockInventory():
    try:
        response = requests.get(baseURL + "/inventory")
        if response.status_code == 200:
            inventory = response.json()
            for item in inventory:
                restockItem(item)
        else:
            print(str(datetime.now()) + " Response status code from a get call to /inventory: " + str(response.status_code))
    except requests.ConnectionError as e:
        print(str(datetime.now()) + " Failed to connect to " + baseURL + " - Retrying")


if __name__ == '__main__':
    try:
        loadConfig()
        baseURL = "http://" + config['inventoryAPI.host'] + ":" + config['inventoryAPI.port']
        while True:
            restockInventory()
            time.sleep(int(config['restock.restockFrequencySeconds']))
    except (KeyboardInterrupt, SystemExit) as e:
        raise e
