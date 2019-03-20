  componentDidMount() {
    Categories.getAll()
      .then((response) => {
        var categories = response.data;
        var sortedCategories = sortBy(categories, "modifiedDate").reverse();
        var items = [];
        sortedCategories.forEach(function(category,index) {
          items.push({
            category_en: category.name.en,
            publish_type: category.publishType,
            edit: <Link to={"/category/"+category.id}>Edit</Link>});
        })
        this.setState({
          data: items
        });
      })
      .catch((err) => {
        alert(err);
      })

      // console.log(this.state.data);
  }


        <Link to="/category/reorder">
       <RaisedButton label="Done" secondary={true} style={styles.addBtn} />
      </Link>






          Categories.updateOrder(this.state)
      .then((response) => {
        alert("Category reordering successful!")
        this.setState({
          selectedCategories: [],
          allCategories: []
        })
      })
      .catch(err => {
        alert(err);
      })
    event.preventDefault();










        var url = constants.UPDATE_CATEGORY + details.id;
    
    var selected_category_ids = []
    details.selectedCategories.forEach(function(item, index){
      selected_category_ids.push(item.id)
    })
    var info = {
      "name": "Category",
      "categoryIds": selected_category_ids

    }
    var options = {
      body: info,
      type: 'json'
    }

    return ServiceHelper.doPutCall(url, options);
  }