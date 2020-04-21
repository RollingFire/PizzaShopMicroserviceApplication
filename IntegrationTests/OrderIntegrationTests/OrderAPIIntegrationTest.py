import requests
import json
import time
from datetime import datetime

CONFIG_SEPARATOR = "="
config = {}
baseURL = ""
custoemrsCreated = []

class AbortTest(Exception):
    def __init__(self, message):
        self.message = message
    def __str__(self):
        return self.message

def loadConfig():
    with open('OrderIntegrationTest.properties') as file:
        for line in file:
            if CONFIG_SEPARATOR in line:
                name, val = line.split(CONFIG_SEPARATOR, 1)
                config[name] = val.rstrip()

def getCustomers():
    try:
        response = requests.get(customerBaseURL + "/api/customer")
        if response.status_code == 200:
            if len(response.json()) > 0:
                return response.json()[0]["id"]
            else:
                response = requests.post(customerBaseURL + "/api/customer")
                if response.status_code == 201:
                    custoemrsCreated.append(response.json()["id"])
                    return response.json()["id"]
                else:
                    raise AbortTest("getCustomers: Failed to create customer. - " + str(response))
        raise AbortTest("getCustomers: Failed to get customers. - " + str(response))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - getCustomers: Error - " + str(e))

def getMenuItems():
    try:
        response = requests.post(menuBaseURL + "/api/menuItem", json={"catagory": "TestCat", "itemName": "TestItemOrder", "discription": "Menu Item created to be used in order integration testing.", "cost": 2.1})
        if response.status_code == 201:
            return response.json()["id"], response.json()["cost"]
        else:
            raise AbortTest("TEST ABORTED - getMenuItems: Failed to create menuItems. Status code: " + str(response.status_code))
    except requests.ConnectionError as e:
        raise AbortTest("TEST ABORTED - getMenuItems: Error - " + str(e))

def cleanUpCreatedCustomers():
    for customerId in custoemrsCreated:
        requests.delete(customerBaseURL + "/api/customer/" + str(customerId))

def createOrder(expected, request):
    try:
        response = requests.post(orderBaseURL + "/api/order", json=request)
        if response.status_code == 201:
            item = response.json()
            expected["order"]["id"] = item["order"]["id"]
            for i in range(len(expected["items"])):
                expected["items"][i]["id"] = item["items"][i]["id"]
                expected["items"][i]["orderId"] = item["items"][i]["orderId"]
            if item == expected:
                print("TEST PASSED - createOrder")
                return item
            else:
                print("TEST FAILED - createOrder: \nExpected - " + str(expected) + " \nActual   - " + str(item))
                return item
        else:
            print("TEST FAILED - createOrder: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - createOrder: Error - " + str(e))

def getOrders(expected=None, filters=""):
    try:
        response = requests.get(orderBaseURL + "/api/order" + filters)
        if response.status_code == 200:
            item = response.json()
            if expected == None:
                return item
            else:
                if expected == item:
                    print("TEST PASSED - gerOrders")
                else:
                    print("TEST FAILED - gerOrders: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            raise AbortTest("TEST ABORTED - gerOrders: Status was " + str(response.status_code))
    except requests.ConnectionError as e:
        raise AbortTest("TEST ABORTED - gerOrders: Error - " + str(e))

def getOrderById(expected, orderId):
    try:
        response = requests.get(orderBaseURL + "/api/order/" + str(orderId))
        if response.status_code == 200:
            item = response.json()
            if expected == item:
                print("TEST PASSED - getOrdersById")
            else:
                print("TEST FAILED - getOrdersById: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            raise AbortTest("TEST ABORTED - getOrdersById: Status was " + str(response.status_code))
    except requests.ConnectionError as e:
        raise AbortTest("TEST ABORTED - getOrdersById: Error - " + str(e))

def updateOrder(expected, orderId, request):
    try:
        response = requests.put(orderBaseURL + "/api/order/" + str(orderId), json=request)
        if response.status_code == 200:
            item = response.json()
            if expected == item:
                print("TEST PASSED - updateOrder")
            else:
                print("TEST FAILED - updateOrder: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            raise AbortTest("TEST ABORTED - updateOrder: Status was " + str(response.status_code))
    except requests.ConnectionError as e:
        raise AbortTest("TEST ABORTED - updateOrder: Error - " + str(e))

def updateOrderStatus(expected, orderId, request):
    try:
        response = requests.put(orderBaseURL + "/api/order/" + str(orderId) + "/status", data=request)
        if response.status_code == 200:
            item = response.json()
            if expected == item:
                print("TEST PASSED - updateOrderStatus")
            else:
                print("TEST FAILED - updateOrderStatus: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            raise AbortTest("TEST ABORTED - updateOrderStatus: " + str(response))
    except requests.ConnectionError as e:
        raise AbortTest("TEST ABORTED - updateOrderStatus: Error - " + str(e))

def getOrderItemsByOrderId(expected, orderId):
    try:
        response = requests.get(orderBaseURL + "/api/order/items/" + str(orderId))
        if response.status_code == 200:
            item = response.json()
            if expected == item:
                print("TEST PASSED - getOrderItemsByOrderId")
            else:
                print("TEST FAILED - getOrderItemsByOrderId: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            raise AbortTest("TEST ABORTED - getOrderItemsByOrderId: Status was " + str(response.status_code))
    except requests.ConnectionError as e:
        raise AbortTest("TEST ABORTED - getOrderItemsByOrderId: Error - " + str(e))

def getOrderItemsByStatus(expected, status):
    try:
        response = requests.get(orderBaseURL + "/api/orderItem/" + status)
        if response.status_code == 200:
            item = response.json()
            for i in expected:
                if i in item:
                    continue
                else:
                    print("TEST FAILED - getOrderItemsByStatus: \nExpected - " + str(expected) + " \nActual   - " + str(item))
                    return
            print("TEST PASSED - getOrderItemsByStatus")
        else:
            raise AbortTest("TEST ABORTED - getOrderItemsByStatus: Status was " + str(response.status_code))
    except requests.ConnectionError as e:
        raise AbortTest("TEST ABORTED - getOrderItemsByStatus: Error - " + str(e))

def getOrderItemById(expected, orderItemId):
    try:
        response = requests.get(orderBaseURL + "/api/orderItem/" + str(orderItemId))
        if response.status_code == 200:
            item = response.json()
            if expected == item:
                print("TEST PASSED - getOrderItemById")
            else:
                print("TEST FAILED - getOrderItemById: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            raise AbortTest("TEST ABORTED - getOrderItemById: Status was " + str(response.status_code))
    except requests.ConnectionError as e:
        raise AbortTest("TEST ABORTED - getOrderItemById: Error - " + str(e))

def createOrderItem(expected, orderId, request):
    try:
        response = requests.post(orderBaseURL + "/api/order/items/" + str(orderId), json=request)
        if response.status_code == 201:
            item = response.json()
            expected["id"] = item["id"]
            if item == expected:
                print("TEST PASSED - createOrderItem")
                return item
            else:
                print("TEST FAILED - createOrderItem: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            print("TEST FAILED - createOrderItem: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - createOrderItem: Error - " + str(e))

def updateOrderItem(expected, orderItemId, request):
    try:
        response = requests.put(orderBaseURL + "/api/orderItem/" + str(orderItemId), json=request)
        if response.status_code == 200:
            item = response.json()
            if expected == item:
                print("TEST PASSED - updateOrderItem")
            else:
                print("TEST FAILED - updateOrderItem: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            raise AbortTest("TEST ABORTED - updateOrderItem: Status was " + str(response.status_code))
    except requests.ConnectionError as e:
        raise AbortTest("TEST ABORTED - updateOrderItem: Error - " + str(e))

def updateOrderItemStatus(expected, orderItemId, request):
    try:
        response = requests.put(orderBaseURL + "/api/orderItem/" + str(orderItemId) + "/status", data=request)
        if response.status_code == 200:
            item = response.json()
            item["cost"] = float('%.4f'%(item["cost"]))
            if expected == item:
                print("TEST PASSED - updateOrderItemStatus")
            else:
                print("TEST FAILED - updateOrderItemStatus: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            raise AbortTest("TEST ABORTED - updateOrderItemStatus: Status was " + str(response.status_code))
    except requests.ConnectionError as e:
        raise AbortTest("TEST ABORTED - updateOrderItemStatus: Error - " + str(e))


if __name__ == '__main__':
    try:
        loadConfig()
        today = datetime.utcnow()
        dateFormated = today.strftime("%Y-%m-%d")
        menuBaseURL = "http://" + config['menuAPI.host'] + ":" + config['menuAPI.port']
        customerBaseURL = "http://" + config['customerAPI.host'] + ":" + config['customerAPI.port']
        orderBaseURL = "http://" + config['orderAPI.host'] + ":" + config['orderAPI.port']
        try:
            customerId = getCustomers()
            menuItemId, menuItemCost = getMenuItems()
            initialOrders = getOrders()
            initialCustomerOrders = getOrders(filters="?customerId=" + str(customerId))

            #Order
            #Post
            request = {
                "customerId": customerId,
                "orderStatus": "OrderAPIIntegrationTest",
                "orderItems": [
                    {
                        "menuItemId": menuItemId,
                        "quantity": 1,
                        "orderItemStatus": "OrderAPIIntegrationTest"
                    }
                ]
            }
            expectedPostReturn = {
                "order": {
                    "id": 0,
                    "customerId": customerId,
                    "orderStatus": request["orderStatus"].upper(),
                    "datePlaced": dateFormated
                },
                "items": [
                    {
                        "id": 0,
                        "orderId": 0,
                        "menuItemId": menuItemId,
                        "quantity": request["orderItems"][0]["quantity"],
                        "orderItemStatus": request["orderItems"][0]["orderItemStatus"].upper(),
                        "cost": menuItemCost * request["orderItems"][0]["quantity"],
                        "lastRevisionDate": dateFormated
                    }
                ]
            }
            orderAndItems = createOrder(expectedPostReturn, request)

            #Get
            getOrders(initialOrders + [orderAndItems["order"]])
            getOrders(initialCustomerOrders + [orderAndItems["order"]], "?customerId=" + str(customerId))
            getOrderById(orderAndItems["order"], orderAndItems["order"]["id"])

            #Put
            updateRequest = {"orderStatus": "OrderAPIIntegrationTestUpdated"}
            orderAndItems["order"]["orderStatus"] = updateRequest["orderStatus"].upper()
            updateOrder(orderAndItems["order"], orderAndItems["order"]["id"], updateRequest)
            getOrderById(orderAndItems["order"], orderAndItems["order"]["id"])

            updateSatusRequest = "OrderAPIIntegrationTestUpdatedAgain"
            orderAndItems["order"]["orderStatus"] = updateSatusRequest.upper()
            updateOrderStatus(orderAndItems["order"], orderAndItems["order"]["id"], updateSatusRequest)
            getOrderById(orderAndItems["order"], orderAndItems["order"]["id"])


            #OrderItem
            #Get
            getOrderItemsByOrderId(orderAndItems["items"], orderAndItems["order"]["id"])
            getOrderItemsByStatus(orderAndItems["items"], orderAndItems["items"][0]["orderItemStatus"])
            getOrderItemById(orderAndItems["items"][0], orderAndItems["items"][0]["id"])

            #Post
            newItemRequest = {
                "menuItemId": menuItemId,
                "quantity": 2,
                "orderItemStatus": "OrderAPIIntegrationTest"
            }
            newItemExpected = {
                "id": 0,
                "orderId": orderAndItems["order"]["id"],
                "menuItemId": menuItemId,
                "quantity": newItemRequest["quantity"],
                "orderItemStatus": newItemRequest["orderItemStatus"].upper(),
                "cost": menuItemCost * newItemRequest["quantity"],
                "lastRevisionDate": dateFormated
            }
            orderAndItems["items"].append(createOrderItem(newItemExpected, orderAndItems["order"]["id"], newItemRequest))
            getOrderItemsByOrderId(orderAndItems["items"], orderAndItems["order"]["id"])

            #Put
            updateItemRequest = {
                "quantity": 3,
                "orderItemStatus": "OrderAPIIntegrationTestUpdated"
            }
            orderAndItems["items"][0]["orderItemStatus"] = updateItemRequest["orderItemStatus"].upper()
            orderAndItems["items"][0]["quantity"] = updateItemRequest["quantity"]
            orderAndItems["items"][0]["cost"] = float('%.4f'%(menuItemCost * updateItemRequest["quantity"]))
            updateOrderItem(orderAndItems["items"][0], orderAndItems["items"][0]["id"], updateItemRequest)
            getOrderItemById(orderAndItems["items"][0], orderAndItems["items"][0]["id"])

            updateItemSatusRequest = "OrderAPIIntegrationTestUpdatedAgain"
            orderAndItems["items"][0]["orderItemStatus"] = updateItemSatusRequest.upper()
            updateOrderItemStatus(orderAndItems["items"][0], orderAndItems["items"][0]["id"], updateItemSatusRequest)
            getOrderItemById(orderAndItems["items"][0], orderAndItems["items"][0]["id"])
            getOrderItemById(orderAndItems["items"][0], orderAndItems["items"][0]["id"])

            print("A order has been created to be used for testing. There is no API call to remove orders from the database. " +
                "Consider loging into the database and removing it manually. The order's id is " + str(orderAndItems["order"]["id"]))
            print("A menuItem has been created to be used for testing. There is no API call to remove menuItems from the database. " +
                "Consider loging into the database and removing it manually. The menuItem's id is " + str(menuItemId))
        except AbortTest as err:
            print(err)
        finally:
            cleanUpCreatedCustomers()
    except (KeyboardInterrupt, SystemExit) as e:
        raise e
