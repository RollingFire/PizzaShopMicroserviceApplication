echo "--------------------Inventory--------------------"
cd InventoryIntegrationTests
py InventoryAPIIntegrationTest.py

echo "--------------------AutoRestocker--------------------"
cd ../AutoRestockerIntegrationTests
py AutoRestockerIntegrationTest.py

echo "--------------------Customer--------------------"
cd ../CustomerIntegrationTests
py CustomerAPIIntegrationTest.py

echo "--------------------Menu--------------------"
cd ../MenuIntegrationTests
py MenuAPIIntegrationTest-Menu.py
py MenuAPIIntegrationTest-MenuItem.py

echo "--------------------Recipe--------------------"
cd ../RecipeIntegrationTests
py RecipeAPIIntegrationTest.py

echo "--------------------Order--------------------"
cd ../OrderIntegrationTests
py OrderAPIIntegrationTest.py