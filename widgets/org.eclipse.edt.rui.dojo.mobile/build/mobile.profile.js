dependencies = {
	action: "release",
	cssOptimize: "comments",
	optimize:"shrinksafe",
	selectorEngine: "acme",
	version: "1.7.1",
	layers: [
		{
			name: "dojo.js",
			boot: true,
			dependencies: [
				"dojox.mobile.app._base",
				"dojox.mobile.app._Widget",
				"dojox.mobile.app._event",
				"dojox.mobile.app._FormWidget",
				"dojox.mobile._base",
				"dojox.mobile.Button",
				"dojox.mobile.View",
				"dojox.mobile.EdgeToEdgeList",
				"dojox.mobile.RoundRectList",
				"dojox.mobile.EdgeToEdgeCategory",
				"dojox.mobile.RoundRectCategory",
				"dojox.mobile.ListItem",
				"dojox.mobile.ProgressIndicator",
				"dojox.mobile.Switch",
				"dojox.mobile.TabBar",
				"dojox.mobile.TabBarButton",
				"dojox.mobile.CheckBox",
				"dojox.mobile.Slider",
				"dojox.mobile.SpinWheel",
				"dojox.mobile.SpinWheelDatePicker",
				"dojox.mobile.SpinWheelTimePicker"
			]
		}
	],
	prefixes: [
		[ "dijit", "../dijit" ],
		[ "dojox", "../dojox" ]
	]
}