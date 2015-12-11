describe('the Tracel parser',function(){
	
	var rootId = 'testContainer';
	
	//Create an easily-removed container for our tests to play in
	beforeEach(function() {
	});
	
	//Clean it up after each spec
	afterEach(function() {
	});
		
	
	
	describe('Property path a.b',function() {
		var lst = tracel.parsePropertyPath("a.b");
		
		beforeEach(function(){
		});
		
		afterEach(function() {
		});
		
		it('has first property "a"',function(){
			expect(lst[0]).toBe("a");
		});

		it('has second property "b"',function(){
			expect(lst[1]).toBe("b");
		});

		
	});		

});