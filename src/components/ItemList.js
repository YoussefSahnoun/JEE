import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Modal, Box, Typography, Button, TextField, AppBar, Toolbar } from '@mui/material';
import "./ItemList.css"

function AddItemModal({ open, onClose, onAdd }) {
    const [itemName, setItemName] = useState('');
    const [itemDescription, setItemDescription] = useState('');
    const [itemPrice, setItemPrice] = useState('');

    const handleAddItem = () => {
        // Validate input
        if (!itemName || !itemDescription || !itemPrice) {
            alert('Please fill out all fields.');
            return;
        }
        // Send new item data to parent component
        onAdd({
            name: itemName,
            description: itemDescription,
            price: parseFloat(itemPrice)
        });
        // Reset input fields
        setItemName('');
        setItemDescription('');
        setItemPrice('');
        // Close the modal
        onClose();
    };

    return (
        <Modal
            open={open}
            onClose={onClose}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
        >
            <Box
                sx={{
                    position: 'absolute',
                    top: '50%',
                    left: '50%',
                    transform: 'translate(-50%, -50%)',
                    width: 400,
                    bgcolor: 'background.paper',
                    border: '2px solid #000',
                    boxShadow: 24,
                    p: 4,
                }}
            >
                <Typography id="modal-modal-title" variant="h6" component="h2">
                    Add New Item
                </Typography>
                <TextField
                    autoFocus
                    margin="dense"
                    label="Name"
                    type="text"
                    fullWidth
                    value={itemName}
                    onChange={(e) => setItemName(e.target.value)}
                />
                <TextField
                    margin="dense"
                    label="Description"
                    type="text"
                    fullWidth
                    value={itemDescription}
                    onChange={(e) => setItemDescription(e.target.value)}
                />
                <TextField
                    margin="dense"
                    label="Price"
                    type="number"
                    fullWidth
                    value={itemPrice}
                    onChange={(e) => setItemPrice(e.target.value)}
                />
                <Button onClick={handleAddItem}>Add Item</Button>
            </Box>
        </Modal>
    );
}

function ItemList() {
    const [items, setItems] = useState([]);
    const [showAddItemModal, setShowAddItemModal] = useState(false);

    useEffect(() => {
        // Fetch all items from the server
        axios.get('http://localhost:8081/api/v1/items')
            .then(response => {
                setItems(response.data);
            })
            .catch(error => {
                console.error('Error fetching items:', error);
            });
    }, []);

    const deleteItem = (id) => {
        // Remove the item from the frontend
        setItems(prevItems => prevItems.filter(item => item.id !== id));

        // Send a DELETE request to delete the item in backend
        axios.delete(`http://localhost:8081/api/v1/items/${id}`)
            .catch(error => {
                console.error('Error deleting item:', error);
            });
    };

    const addItem = (newItem) => {
        // Add the new item to the frontend
        setItems([...items, newItem]);

        // Send a POST request to add a new item in backend
        axios.post('http://localhost:8081/api/v1/items', newItem)
            .catch(error => {
                console.error('Error adding item:', error);
            });

        // Close the add item modal
        setShowAddItemModal(false);
    };

    const logout= () => {
        const response =  axios.get('http://localhost:8081/api/users/logout')
        if (response) {
            // Redirect to items page on successful registration
            window.location = 'http://localhost:8081/login.html';
        }
    };

    return (
        <div>
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        My App
                    </Typography>
                    <Button color="inherit" onClick={logout}>Logout</Button>
                </Toolbar>
            </AppBar>
            <div className="container">
                <div className="item-list">
                    <h1 className={"title"}>Items</h1>
                    <ul>
                        {items.map(item => (
                            <li key={item.id} className="item">
                                <Typography variant="h2">{item.name}</Typography>
                                <Typography variant="body1">{item.description}</Typography>
                                <Typography variant="body2">Price: {item.price}</Typography>
                                <Button onClick={() => deleteItem(item.id)}>Delete</Button>
                            </li>
                        ))}
                    </ul>
                </div>
                <Button className="add-item-button" variant="contained" onClick={() => setShowAddItemModal(true)}>Add Item</Button>
                <AddItemModal
                    open={showAddItemModal}
                    onClose={() => setShowAddItemModal(false)}
                    onAdd={addItem}
                />
            </div>
        </div>
    );

}

export default ItemList;
