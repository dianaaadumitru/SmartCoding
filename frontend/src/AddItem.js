import { useRef } from "react";
import { FaPlus } from "react-icons/fa";

const AddItem = ( {newItem, setNewItem, handleSubmit} ) => {
    const inputRef = useRef();

    return (
        <form name="addForm" onSubmit={(e) => handleSubmit(e)}>
            {/* <label htmlFor="addItem">Add item</label> */}
            <input
                autoFocus
                ref={inputRef}
                id="addItem"
                type="text"
                placeholder="Add item"
                required
                value={newItem}
                onChange={(e) => setNewItem(e.target.value)}
            />
            <button
                type='submit'
                aria-label="Add Item"
                onClick={() => inputRef.current.focus()}
            ><FaPlus /></button>

        </form>
    )
}

export default AddItem;