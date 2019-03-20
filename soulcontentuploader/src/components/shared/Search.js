/**
 * Autocomplete Search component
**/
import React, { Component } from 'react'
import PropTypes from 'prop-types'
import TextField from 'material-ui/TextField'
import ReactDOM from 'react-dom'
import {SortableContainer, SortableElement, arrayMove} from 'react-sortable-hoc';
import _ from "lodash";

const SortableItem = SortableElement(({value, items, index}) => {
  const { multiple } = items.props;
  let itemClass = 'autocomplete__item autocomplete__item--selected autocomplete__item__dropdown'
  let dropDown = <span className='autocomplete__dropdown' />
  let icon = <span data-id={value.id} className='autocomplete__close' onTouchTap={items.handleRemove.bind(this)} > </span>

  if(multiple) {
    dropDown = null
    itemClass = 'autocomplete__item autocomplete__item--selected'
  }

  return(<li key={index} className={itemClass} >
          <span data-id={value.id} dangerouslySetInnerHTML={{__html: value.value }}></span>
          { items.props.autoCompleteVisibility && icon }
          { dropDown }
        </li>);
});

const SortableList = SortableContainer(({items}) => {
  const { selectedItems } = items.state;
  return (
    <ul className='autocomplete__items'>
      {selectedItems.map((value, index) => (
        <SortableItem key={`item-${index}`} index={index} value={value} items={items}/>
      ))}
    </ul>
  );
});

export default class Search extends Component {

  static get defaultProps () {
    return {
      placeholder: '— None',
      NotFoundPlaceholder: 'Please search for some items...',
      maxSelected: 500,
      multiple: false
    }
  }

  static get propTypes () {
    return {
      items: PropTypes.arrayOf(PropTypes.object).isRequired,
      initialSelected: PropTypes.oneOfType([
        PropTypes.object,
        PropTypes.arrayOf(PropTypes.object)
      ]),
      onItemsChanged: PropTypes.func,
      placeholder: PropTypes.string,
      NotFoundPlaceholder: PropTypes.string,
      maxSelected: PropTypes.number,
      multiple: PropTypes.bool,
      onKeyChange: PropTypes.func,
      getItemsAsync: PropTypes.func
    }
  }

  constructor (props) {
    super(props)
    this.state = {
      menuItems: [],
      selectedItems: [],
      searchValue: '',
      menuVisible: false
    }
  }

  componentDidMount() {
    const { initialSelected } = this.props;
    if(initialSelected instanceof Array) {
      this.setSelected(initialSelected)
    } else {
      this.addSelected(initialSelected)
    }
  }

  objectsAreSame(x, y) {
    var objectsAreSame = true;
    for(var propertyName in x) {
      if(!_.isEqual(x[propertyName],y[propertyName])) {
         objectsAreSame = false;
         break;
      }
    }
    return objectsAreSame;
  }

  componentDidUpdate(prevProps, prevState){
    if(!this.objectsAreSame(this.props.initialSelected, prevProps.initialSelected)){
      const { initialSelected } = this.props;
      if(initialSelected instanceof Array) {
        this.setSelected(initialSelected)
      } else {
        this.addSelected(initialSelected)
      }
    }
  }

  SearchItemInArrayObjects(items, input, searchKey) {
    var reg = new RegExp(input.split('').join('\\w*').replace(/\W/, ''), 'i')
    return items.filter((item) => {
      if (reg.test(item[searchKey])) {
        return item
      }
      return {}
    })
  }

  selectMenuItem (item) {
    const { multiple } = this.props;
    multiple ? this.addSelected(item) : this.setSelected( [item] )
    this.hideMenu()
  }

  showMenu() {
    this.setState({menuVisible: true })
  }

  hideMenu() {
    this.setState({menuVisible: false })
    this.resetPlaceholder()
  }

  triggerItemsChanged() {
    if (this.props.onItemsChanged !== undefined) {
      this.props.onItemsChanged(this.state.selectedItems)
    }
  }

  triggerKeyChange(searchValue) {
    if (this.props.onKeyChange !== undefined) {
      this.props.onKeyChange(searchValue)
    }
  }

  triggerGetItemsAsync(searchValue) {
    if (this.props.getItemsAsync !== undefined) {
      this.props.getItemsAsync(searchValue, () => {
        this.updateSearchValue(searchValue)
      })
    }
  }

  setSelected(selected) {
    this.setState({selectedItems: selected }, () => {
      this.triggerItemsChanged()
    })
  }

  addSelected(selected) {
    let items = this.state.selectedItems
    items.push(selected)
    this.setState({selectedItems: items }, () => {
      this.triggerItemsChanged()
    })
  }

  removeSelected(itemId) {
    let items = this.state.selectedItems
    let itemsUpdated = items.filter( (i) => {
       return i.id !== itemId
    })
    this.setState({selectedItems: itemsUpdated }, () => {
      this.triggerItemsChanged()
    })
  }

  updateSearchValue(value) {
    const { items } = this.props;
    this.setState({ searchValue: value }, () => {
      let menuItems = this.SearchItemInArrayObjects(items, this.state.searchValue, 'value')
      this.setMenuItems(menuItems)
    })
  }

  showAllMenuItems() {
    const { items } = this.props;
    this.setState({searchValue: ''})
    let menuItems = this.SearchItemInArrayObjects(items, '', 'value')
    this.setMenuItems(menuItems)
  }

  setMenuItems(items) {
    const { getItemsAsync } = this.props;
    this.setState({menuItems: items})
    if(items.length || getItemsAsync !== undefined){
      this.showMenu()
    } else {
      this.hideMenu()
    }
  }

  itemSelected(itemId) {
    const { selectedItems } = this.state;
    let item = selectedItems.find( (s) => {
        return s.id === itemId;
    });
    return (item !== undefined)
  }

  focusInput() {
    this.showAllMenuItems()
    ReactDOM.findDOMNode(this.refs.searchInput).placeholder = ''
    ReactDOM.findDOMNode(this.refs.searchInput).value = ''
    this.blurTimeout = setTimeout(() => {
      ReactDOM.findDOMNode(this.refs.searchInput).focus()
    }, 100);
  }

  blurInput() {
    this.blurTimeout = setTimeout(() => {
      ReactDOM.findDOMNode(this.refs.searchInput).blur()
      this.hideMenu()
    }, 100);
  }

  resetPlaceholder() {
    let placeholder = ReactDOM.findDOMNode(this.refs.placeholder)
    placeholder = this.props.placeholder
  }

  handleRemove(e) {

    e.preventDefault()
    e.stopPropagation()
    this.removeSelected(e.target.dataset.id)
  }

  handleFocus(e) {
    this.focusInput()
  }

  handleBlur(e) {
    this.blurInput()
  }

  handleClick(e) {
    this.focusInput()
  }

  handleItemClick(e) {
    this.focusInput()
  }

  handleSelect(e) {
    let element = e.currentTarget.children[0]
    let item = { id: element.dataset.id, value: element.innerHTML.replace(/&amp;/g, '&') }
    this.selectMenuItem(item)
  }

  handleKeyChange (e) {
    const { getItemsAsync } = this.props;
    let value = this.refs.searchInput.value
    this.triggerKeyChange(value)
    if( getItemsAsync !== undefined ) {
      this.triggerGetItemsAsync(value)
    } else {
      this.updateSearchValue(value)
    }
  }

  renderMenuItems() {
    const { menuItems } = this.state;
    const { NotFoundPlaceholder } = this.props;
    if(!menuItems.length) {
      return (
        <li className='autocomplete__item autocomplete__item--disabled'>
          <span data-id={0}>{NotFoundPlaceholder}</span>
        </li>
      )
    }

    let items = menuItems.map((item, i) => {
      if(this.itemSelected(item.id)){
        return (
          <li key={i} className='autocomplete__item autocomplete__item--disabled'>
            <span key={i} data-id={item.id} dangerouslySetInnerHTML={{__html: item.value }}></span>
          </li>
        )
      } else {
        return (
          <li key={i} className='autocomplete__item' onTouchTap={this.handleSelect.bind(this)}>
            <span key={i} data-id={item.id} dangerouslySetInnerHTML={{__html: item.value }}></span>
          </li>
        )
      }
    })
    return items
  }

  renderInput() {
    const { maxSelected, multiple } = this.props;
    const { selectedItems } = this.state;
    let inputClass = 'autocomplete__input'
    if(multiple && selectedItems.length >= maxSelected ){
      inputClass = 'autocomplete__input autocomplete__input--hidden'
    }

    let textStyle = {
      width: '100%'
    }
    return (
      <TextField style={textStyle}
             className={inputClass}
             ref='searchInput'
             floatingLabelText={this.props.placeholder}
             onTouchTap={this.handleClick.bind(this)}
             onFocus={this.handleFocus.bind(this)}
             onBlur={this.handleBlur.bind(this)}
             onKeyUp={this.handleKeyChange.bind(this)} />
    )
  }

  getMenuClass() {
    const { maxSelected, multiple } = this.props;
    const { menuVisible, selectedItems } = this.state;
    let menuClass = 'autocomplete__menu autocomplete__menu--hidden'
    if(menuVisible && !multiple){
      menuClass = 'autocomplete__menu'
    }
    if(menuVisible && selectedItems.length < maxSelected ){
      menuClass = 'autocomplete__menu'
    }
    return menuClass
  }

  onSortEnd(oldIndex, newIndex) {
    if((oldIndex.oldIndex === oldIndex.newIndex) && (newIndex.screenX > 1005 && newIndex.screenX < 1025)){
      let items = this.state.selectedItems
      items.splice(oldIndex.oldIndex, 1)
      this.setState({selectedItems: items }, () => {
        this.triggerItemsChanged()
      })
    }
    else{
      this.props.onItemsChanged(arrayMove(this.state.selectedItems, oldIndex.oldIndex, oldIndex.newIndex))
    }
  };

  render () {
    const { multiple} = this.props;

    let menuClass = this.getMenuClass();
    const autoCompleteDropdown = this.props.autoCompleteVisibility;

    if(autoCompleteDropdown){
      return (
        <div className='autocomplete'>
          <div className='autocomplete__selected'>
            <SortableList items={this} onSortEnd={this.onSortEnd.bind(this)} />
          </div>
  
          { multiple && this.renderInput() }
  
          <div className='autocomplete__menu--wrap'>
            <div className={menuClass} ref='autocomplete'>
              { !multiple && this.renderInput() }
              <ul className='autocomplete__items'>
                {this.renderMenuItems()}
              </ul>
            </div>
          </div>
          
        </div>
      )

    }else{

      return (
        <div className='autocomplete'>
          <div className='autocomplete__selected'>
            <SortableList items={this} onSortEnd={this.onSortEnd.bind(this)} />
          </div>
        </div>
      )
    }
  }
}
