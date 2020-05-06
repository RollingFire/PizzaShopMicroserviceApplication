export interface InventoryItem {
    id: number,
    name: string ,
    units: number,
    unitType: string,
    restockAt: number,
    restockAmount: number
}