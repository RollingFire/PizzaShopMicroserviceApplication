echo "--------------------Setting up Network--------------------"
docker network create pizza-shop
echo "--------------------Building Database--------------------"
cd Database
./BuildSQL.bash
echo "--------------------Building Inventory--------------------"
cd ../InventoryMicroservice
./BuildInventory.bash
echo "--------------------Building AutoRestocker--------------------"
cd ../AutoRestocker
./BuildAutoRestocker.bash
echo "--------------------Building Customer--------------------"
cd ../CustomerMicroservice
./BuildCustomer.bash
echo "--------------------Building Menu--------------------"
cd ../MenuMicroservice
./BuildMenu.bash
echo "--------------------Building Recipe--------------------"
cd ../RecipeMicroservice
./BuildRecipe.bash
echo "--------------------Building Order--------------------"
cd ../OrderMicroservice
./BuildOrder.bash
echo "--------------------Building Frontend--------------------"
cd ../Frontend/PizzaShop
./BuildFrontend.bash